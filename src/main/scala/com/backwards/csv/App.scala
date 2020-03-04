package com.backwards.csv

import java.nio.file.{Path, Paths}
import cats.data.EitherT
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import cats.instances.int._
import cats.syntax.all._
import fs2._

object App extends IOApp {
  def parse(csvPath: Path, hasHeader: Boolean, csvParser: CsvParser): Stream[IO, Unit] =
    Stream.resource(Blocker[IO]).flatMap { blocker =>
      io.file
        .readAll[IO](csvPath, blocker, 4 * 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .drop(if (hasHeader) 1 else 0)
        .scan(Lines())(Line.conflate(csvParser.csvConfig.quote))
        .filter { case Lines(_, line) =>
          line.trim.nonEmpty
        }
        .map { case Lines(_, line) =>
          csvParser.parse(line)
        }
        .foldMap {
          case Right(lines) =>
            scribe.info(lines.flatten.mkString(csvParser.delimiter))
            1
          case Left(errorMessage) =>
            scribe.error(errorMessage)
            0
        }
        .map { n =>
          scribe.info(s"End of CSV of $n valid line${if (n == 1) "" else "s"}")
        }
    }

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      args <- EitherT.fromOption[IO](ArgsParser.parse(args), "Failed to parse application arguments")
      _ = scribe.info(args.show)
      csvConfig <- EitherT.fromEither[IO](CsvConfig().leftMap(_.prettyPrint()))
      _ = scribe.info(csvConfig.show)
    } yield {
      val csvParser = new CsvParser(csvConfig)
      parse(Paths.get(args.csv.toURI), args.hasHeader, csvParser).compile.drain.as(ExitCode.Success)
    }).value.flatMap {
      case Left(error) => IO.raiseError(new Exception(error))
      case Right(io) => io
    }
}