package com.backwards.csv

import java.nio.file.{Path, Paths}
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import cats.implicits._
import fs2._

object App extends IOApp {
  def parse(csvPath: Path, csvParser: CsvParser): Stream[IO, Int] =
    Stream.resource(Blocker[IO]).flatMap { blocker =>
      io.file
        .readAll[IO](csvPath, blocker, 4 * 4096)
        .map(_.toChar.toString)
        .append(Stream.eval(IO.pure(csvParser.csvConfig.lineDelimiter)))
        .scan(Line())(Line.conflate(csvParser.csvConfig))
        .filter { case Line(_, line) =>
          line.trim.nonEmpty
        }
        .drop(if (csvParser.csvConfig.header) 1 else 0)
        .map { case Line(_, line) =>
          csvParser.parse(line)
        }
        .foldMap {
          case Right(lines) =>
            scribe.info(lines.flatten.map(_.replaceAll(csvParser.csvConfig.lineDelimiter, " ")).show)
            1
          case Left(errorMessage) =>
            scribe.error(errorMessage)
            0
        }
        .evalTap { n =>
          IO {
            scribe.info(s"End of CSV of $n valid line${if (n == 1) "" else "s"}")
            n
          }
        }
    }

  override def run(args: List[String]): IO[ExitCode] =
    ArgsParser.parse(args).fold(IO(ExitCode.Error)) { csv =>
      scribe.info(csv.show)

      parse(
        Paths.get(csv.file.toURI),
        new CsvParser(csv.csvConfig)
      ).compile.drain.as(ExitCode.Success)
    }
}