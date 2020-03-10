package com.backwards.csv

import cats.Show
import monocle.macros.Lenses
import shapeless.tag.@@
import com.backwards.tag._

@Lenses
final case class CsvConfig(
  header: Boolean @@ Header = false.tag[Header],
  quote: String @@ Quote = "\"".tag[Quote],
  fieldDelimiter: String @@ FieldDelimiter = ",".tag[FieldDelimiter],
  lineDelimiter: String @@ LineDelimiter = "\n".tag[LineDelimiter]
)

object CsvConfig {
  implicit val csvConfigShow: Show[CsvConfig] =
    c => s"CsvConfig(header = ${c.header} quote = ${c.quote}, fieldDelimiter = ${c.fieldDelimiter}, lineDelimiter = ${c.lineDelimiter})"

  def apply: CsvConfig = CsvConfig()
}

trait Header

trait Quote

trait FieldDelimiter

trait LineDelimiter