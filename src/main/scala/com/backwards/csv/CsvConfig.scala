package com.backwards.csv

import cats.Show
import cats.syntax.show._
import monocle.macros.Lenses

@Lenses
final case class CsvConfig(
  header: Header = Header(),
  quote: Quote = Quote(),
  fieldDelimiter: FieldDelimiter = FieldDelimiter(),
  lineDelimiter: LineDelimiter = LineDelimiter()
)

object CsvConfig {
  implicit val csvConfigShow: Show[CsvConfig] =
    c => s"CsvConfig(header = ${c.header.show} quote = ${c.quote.show}, fieldDelimiter = ${c.fieldDelimiter.show}, lineDelimiter = ${c.lineDelimiter.show})"
}

final case class Header(value: Boolean = false) extends AnyVal

object Header {
  implicit val headerShow: Show[Header] =
    h => s"Header(value = ${h.value}"
}

final case class Quote(value: String = "\"") extends AnyVal

object Quote {
  implicit val quoteShow: Show[Quote] =
    q => s"Quote(value = ${q.value}"
}

final case class FieldDelimiter(value: String = ",") extends AnyVal

object FieldDelimiter {
  implicit val fieldDelimiterShow: Show[FieldDelimiter] =
    f => s"FieldDelimiter(value = ${f.value}"
}

final case class LineDelimiter(value: String = "\n") extends AnyVal

object LineDelimiter {
  implicit val lineDelimiterShow: Show[LineDelimiter] =
    l => s"LineDelimiter(value = ${l.value}"
}