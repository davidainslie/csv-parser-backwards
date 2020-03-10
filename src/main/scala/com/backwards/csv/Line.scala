package com.backwards.csv

import java.lang.System.lineSeparator

/**
 * A line is either singular, or can be conflated from multiple lines by "quoting" text, where the "quote" is configurable.
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

    println(accumulatedChars.mkString.count(_.toString == csvConfig.quote.value) % 2 == 0)
    println(accumulatedChars.mkString.endsWith(csvConfig.lineDelimiter.value))

    if (accumulatedChars.mkString.count(_.toString == csvConfig.quote.value) % 2 == 0 &&
        accumulatedChars.mkString.endsWith(csvConfig.lineDelimiter.value)) {
      Line(Nil, accumulatedChars.mkString.stripSuffix(csvConfig.lineDelimiter.value) + lineSeparator)
    } else {
      Line(accumulatedChars)
    }


    /*if (accumulatedChars.mkString.count(_.toString == quote.value) % 2 == 0) {
      Line(Nil, accumulatedChars.mkString(" "))
    } else {
      Line(accumulatedChars)
    }*/
  }
}