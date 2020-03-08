package com.backwards.csv

import java.io.File
import scala.Function.{uncurried => uncurry}
import scala.util.matching.Regex
import scopt.OParser

object ArgsParser {
  lazy val file: File => Csv => Csv =
    Csv.file.set

  lazy val header: Boolean => Csv => Csv =
    Header.apply _ andThen (Csv.csvConfig composeLens CsvConfig.header).set

  lazy val quote: String => Csv => Csv =
    filter andThen Quote.apply andThen (Csv.csvConfig composeLens CsvConfig.quote).set

  lazy val fieldDelimiter: String => Csv => Csv =
    filter andThen FieldDelimiter.apply andThen (Csv.csvConfig composeLens CsvConfig.fieldDelimiter).set

  lazy val lineDelimiter: String => Csv => Csv =
    filter andThen LineDelimiter.apply andThen (Csv.csvConfig composeLens CsvConfig.lineDelimiter).set

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
        opt[File]('c', "csv")
          .required()
          .valueName("<file>")
          .action(uncurry(file))
          .text("csv is a required file property - relative or absolute path"),
        opt[Boolean]('h', "header")
          .optional()
          .action(uncurry(header))
          .text(s"header is an optional boolean property - whether the given csv includes a header: ${Header().value} by default"),
        opt[String]('q', "quote")
          .optional()
          .action(uncurry(quote))
          .text(s"""quote is an optional string property - allowing a line to span multiple lines: ${Quote().value} by default"""),
        opt[String]('f', "fieldDelimiter")
          .optional()
          .action(uncurry(fieldDelimiter))
          .text(s"""fieldDelimiter is an optional string property - denoting field separation: ${FieldDelimiter().value} by default"""),
        opt[String]('l', "lineDelimiter")
          .optional()
          .action(uncurry(lineDelimiter))
          .text(s"""lineDelimiter is an optional string property - denoting line separation: ${LineDelimiter().value} by default""")
      )
    }

    OParser.parse(argsParser, args, Csv(new File(".")))
  }
}