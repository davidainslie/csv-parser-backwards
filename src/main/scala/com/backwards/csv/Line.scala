package com.backwards.csv

import java.lang.System.lineSeparator

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
  def conflate(csvConfig: CsvConfig)(line: Line, chars: String): Line = {
    val accumulatedChars = line.accumulatedChars :+ chars

    if (accumulatedChars.mkString.count(_.toString == csvConfig.quote) % 2 == 0 &&
        accumulatedChars.mkString.endsWith(csvConfig.lineDelimiter)) {
      Line(Nil, accumulatedChars.mkString.stripSuffix(csvConfig.lineDelimiter) + lineSeparator)
    } else {
      Line(accumulatedChars)
    }
  }
}