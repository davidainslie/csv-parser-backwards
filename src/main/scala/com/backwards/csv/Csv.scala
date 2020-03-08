package com.backwards.csv

import java.io.File
import cats.Show
import cats.syntax.show._
import monocle.macros.Lenses

@Lenses
final case class Csv(file: File, csvConfig: CsvConfig = CsvConfig())

object Csv {
  implicit val csvShow: Show[Csv] =
    c => s"Csv(file = ${c.file.getName}, csvConfig = ${c.csvConfig.show})"
}