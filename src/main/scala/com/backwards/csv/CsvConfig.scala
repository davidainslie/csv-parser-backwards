package com.backwards.csv

import cats.Show
import cats.syntax.show._
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures

final case class CsvConfig(quote: Quote = Quote(), fieldDelimiter: FieldDelimiter = FieldDelimiter(), lineDelimiter: LineDelimiter = LineDelimiter())

/**
 * Defaults to loading configuration from "application.conf" on classpath. This can be overridden as follows:
 * config.resource specifies a resource name - not a basename, i.e. application.conf not application
 * config.file specifies a filesystem path, again it should include the extension, not be a basename
 * config.url specifies a URL
 * e.g.
 * -Dconfig.file=path/to/config-file
 */
object CsvConfig extends PureConfig {
  implicit val csvConfigShow: Show[CsvConfig] =
    config => s"CsvConfig(quote = ${config.quote.show}, fieldDelimiter = ${config.fieldDelimiter.show}, lineDelimiter = ${config.lineDelimiter.show})"

  def apply(): ConfigReaderFailures Either CsvConfig =
    ConfigSource.default.at("csv").load[CsvConfig]

  def apply(csvConfigString: String): ConfigReaderFailures Either CsvConfig =
    ConfigSource.string(csvConfigString).load[CsvConfig]
}

final case class Quote(value: String = "\"") extends AnyVal

object Quote {
  implicit val quoteShow: Show[Quote] =
    quote => s"Quote(value = ${quote.value}"
}

final case class FieldDelimiter(value: String = ",") extends AnyVal

object FieldDelimiter {
  implicit val fieldDelimiterShow: Show[FieldDelimiter] =
    fieldDelimiter => s"FieldDelimiter(value = ${fieldDelimiter.value}"
}

final case class LineDelimiter(value: String = "\n") extends AnyVal

object LineDelimiter {
  implicit val lineDelimiterShow: Show[LineDelimiter] =
    lineDelimiter => s"LineDelimiter(value = ${lineDelimiter.value}"
}