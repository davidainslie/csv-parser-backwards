package com.backwards.csv

/**
 * A line is either singular, or can be conflated from multiple lines by "quoting" text, where the "quote" is configurable.
 * e.g. a multi-line using the default quote:
 * <pre>
 *  cell,"a split
 *  cell"
 * </pre>
 */
object Line {
  def conflate(quote: Quote = Quote())(lines: Lines, line: String): Lines =
    if ((lines.accumulatedLines.mkString + line).count(_.toString == quote.value) % 2 == 0)
      Lines(Nil, lines.accumulatedLines.mkString(" ") + line)
    else
      Lines(lines.accumulatedLines :+ line)
}