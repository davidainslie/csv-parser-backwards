package com.backwards.csv

import java.io.File
import scala.Function.{uncurried => uncurry}
import scala.util.chaining._
import scala.util.matching.Regex
import scopt.OParser
import scribe.Level
import com.backwards.tag._

object ArgsParser {
  lazy val logLevel: String => Csv => Csv = {
    def logLevel(level: Level): Csv => Csv = {
      scribe.Logger.root.clearHandlers().clearModifiers().withHandler(minimumLevel = Some(level)).replace()
      identity
    }

    _.toLowerCase match {
      case "debug" => logLevel(Level.Debug)
      case "warn" => logLevel(Level.Warn)
      case "error" => logLevel(Level.Error)
      case _ => logLevel(Level.Info) // Default TODO - Bit of a cop-out but added last minute and will revise one day.
    }
  }

  lazy val file: File => Csv => Csv =
    Csv.file.set

  lazy val header: Boolean => Csv => Csv =
    _.tag[Header] pipe (Csv.csvConfig composeLens CsvConfig.header).set

  lazy val quote: String => Csv => Csv =
    filter andThen (_.tag[Quote] pipe (Csv.csvConfig composeLens CsvConfig.quote).set)

  lazy val fieldDelimiter: String => Csv => Csv =
    filter andThen (_.tag[FieldDelimiter] pipe (Csv.csvConfig composeLens CsvConfig.fieldDelimiter).set)

  lazy val lineDelimiter: String => Csv => Csv =
    filter andThen (_.tag[LineDelimiter] pipe (Csv.csvConfig composeLens CsvConfig.lineDelimiter).set)

  lazy val Filter: Regex = "\"(.*)\"".r

  lazy val filter: String => String = {
    case Filter(s) => s
    case s => s
  }

  // TODO - Should this give IO[Option[Csv]]? After all, it does access IO
  def parse(args: List[String]): Option[Csv] = {
    val argsParser: OParser[Unit, Csv] = {
      val argsbuilder = OParser.builder[Csv]

      import argsbuilder._

      OParser.sequence(
        programName("csv"),
        opt[String]("logLevel")
          .optional()
          .action(uncurry(logLevel))
          .text(s"""logLevel is an optional string property - denoting log level of debug, info, warn or error: info by default"""),
        opt[File]('c', "csv")
          .required()
          .valueName("<file>")
          .action(uncurry(file))
          .text("csv is a required file property - relative or absolute path"),
        opt[Boolean]('h', "header")
          .optional()
          .action(uncurry(header))
          .text(s"header is an optional boolean property - whether the given csv includes a header: ${CsvConfig.header} by default"),
        opt[String]('q', "quote")
          .optional()
          .action(uncurry(quote))
          .text(s"""quote is an optional string property - allowing a line to span multiple lines: ${CsvConfig.quote} by default"""),
        opt[String]('f', "fieldDelimiter")
          .optional()
          .action(uncurry(fieldDelimiter))
          .text(s"""fieldDelimiter is an optional string property - denoting field separation: ${CsvConfig.fieldDelimiter} by default"""),
        opt[String]('l', "lineDelimiter")
          .optional()
          .action(uncurry(lineDelimiter))
          .text(s"""lineDelimiter is an optional string property - denoting line separation: ${CsvConfig.lineDelimiter} by default""")
      )
    }

    OParser.parse(argsParser, args, Csv(new File(".")))
  }
}