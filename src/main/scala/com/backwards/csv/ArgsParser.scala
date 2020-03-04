package com.backwards.csv

import java.io.File
import scala.Function.{uncurried => uncurry}
import monocle.Lens
import monocle.macros.GenLens
import scopt.OParser

object ArgsParser {
  lazy val csvLens: Lens[Args, File] = GenLens[Args](_.csv)
  lazy val hasHeaderLens: Lens[Args, Boolean] = GenLens[Args](_.hasHeader)

  val csv: File => Args => Args =
    csvLens.set

  val hasHeader: Boolean => Args => Args =
    hasHeaderLens.set

  def parse(args: List[String]): Option[Args] = {
    val argsParser: OParser[Unit, Args] = {
      val argsbuilder = OParser.builder[Args]

      import argsbuilder._

      OParser.sequence(
        programName("csv"),
        opt[File]('c', "csv")
          .required()
          .valueName("<file>")
          .action(uncurry(csv))
          .text("csv is a required file property - relative or absolute path"),
        opt[Boolean]('h', "hasHeader")
          .action(uncurry(hasHeader))
          .text("hasHeader is a boolean property - whether the given csv includes a header, false by default")
      )
    }

    OParser.parse(argsParser, args, Args())
  }
}