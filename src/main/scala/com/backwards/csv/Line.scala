package com.backwards.csv

import java.lang.System.lineSeparator
import shapeless.tag.@@
import com.backwards.tag._

/**
 * A line is either singular, or can be conflated from multiple lines by "quoting" text, where "quote" and "line delimiter" are configurable.
 * e.g. a multi-line using the default quote:
 * <pre>
 *  cell,"a split
 *  cell"
 * </pre>
 */
final case class Line(header: Boolean @@ Header, accumulatedChars: List[String] = Nil, parsedLine: Option[String] = None)

object Line {
  val excludesHeader: Line =
    Line(false.tag[Header])

  def excludesHeader(accumulatedChars: List[String]): Line =
    Line(false.tag[Header], accumulatedChars)

  def excludesHeader(parsedLine: Option[String]): Line =
    Line(false.tag[Header], parsedLine = parsedLine)

  def parse(csvParser: CsvParser)(line: Line, chars: String): Line = {
    import csvParser.csvConfig._

    val accumulatedChars = line.accumulatedChars :+ chars
    val contiguousAccumulatedChars = accumulatedChars.mkString

    if (contiguousAccumulatedChars.count(_.toString == quote) % 2 == 0 &&
        contiguousAccumulatedChars.endsWith(lineDelimiter)) {

      csvParser.parse(contiguousAccumulatedChars.stripSuffix(lineDelimiter) + lineSeparator) match {
        case Right(parsed) =>
          if (line.header) {
            excludesHeader
          } else {
            val parsedLine = parsed.flatten.map(_.replaceAll(lineDelimiter, " ")).mkString
            scribe.info(parsedLine)
            excludesHeader(parsedLine = Option(parsedLine))
          }

        case Left(errorMessage) =>
          scribe.error(errorMessage)
          excludesHeader
      }
    } else {
      Line(line.header, accumulatedChars)
    }
  }
}