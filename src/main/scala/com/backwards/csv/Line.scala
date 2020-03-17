package com.backwards.csv

import java.lang.System.lineSeparator
import cats.Show
import cats.implicits._

/**
 * A line is either singular, or can be conflated from multiple lines by "quoting" text, where "quote" and "line delimiter" are configurable.
 * e.g. a multi-line using the default quote:
 * <pre>
 *  cell,"a split
 *  cell"
 * </pre>
 */
final case class Line(accumulatedChars: List[String] = Nil, line: String = "")

object Line {
  implicit val lineShow: Show[Line] =
    line => s"Line(accumulatedChars = ${line.accumulatedChars}, line = ${line.line})"

  def conflate(csvConfig: CsvConfig)(line: Line, chars: String): Line = {
    scribe.debug(line.show)

    val accumulatedChars = line.accumulatedChars :+ chars
    val contiguousAccumulatedChars = accumulatedChars.mkString

    if (contiguousAccumulatedChars.count(_.toString == csvConfig.quote) % 2 == 0 &&
        contiguousAccumulatedChars.endsWith(csvConfig.lineDelimiter)) {
      Line(Nil, contiguousAccumulatedChars.stripSuffix(csvConfig.lineDelimiter) + lineSeparator)
    } else {
      Line(accumulatedChars)
    }
  }
}