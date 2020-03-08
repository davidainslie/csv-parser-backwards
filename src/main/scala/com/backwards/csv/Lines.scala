package com.backwards.csv

/**
 * A line is either singular, or can be conflated from multiple lines by "quoting" text, where the "quote" is configurable.
 * e.g. a multi-line using the default quote:
 * <pre>
 *  cell,"a split
 *  cell"
 * </pre>
 */
final case class Lines(accumulatedLines: List[String] = Nil, line: String = "")

object Lines {
  def conflate(quote: Quote)(lines: Lines, line: String): Lines = {
    val accumulatedLines = lines.accumulatedLines :+ line

    if (accumulatedLines.mkString.count(_.toString == quote.value) % 2 == 0) {
      Lines(Nil, accumulatedLines.mkString(" "))
    } else {
      Lines(accumulatedLines)
    }
  }
}