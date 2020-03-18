package com.backwards.csv

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.tag._

class LineSpec extends AnyWordSpec with Matchers {
  def parse(chars: String)(implicit csvParser: CsvParser): Line =
    Line.parse(csvParser)(Line(false.tag[Header]), chars)

  "Line with default quote" should {
    implicit val csvParser: CsvParser = new CsvParser(CsvConfig())

    import csvParser.csvConfig._

    "be parsed from simple/single line" in {
      val line = s"My line"

      parse(s"$line$lineDelimiter") mustBe Line(parsedLine = Option(line))
    }

    "be parsed from multiple lines" in {
      val line = s"My$lineDelimiter ${quote}line$quote complete"

      parse(s"$line$lineDelimiter") mustBe Line( parsedLine = Option("My line complete"))
    }

    "be pending parse" in {
      val line = s"My ${quote}line"

      parse(line) mustBe Line(accumulatedChars = List(line))
    }

    "be pending parse where new line would be embedded in quotes" in {
      val line = s"My ${quote}line$lineDelimiter"

      parse(line) mustBe Line(accumulatedChars = List(line))
    }
  }

  "Line with overriding quote" should {
    implicit val csvParser: CsvParser = new CsvParser(CsvConfig(quote = "#".tag[Quote]))

    import csvParser.csvConfig._

    "be parsed from simple/single line" in {
      val line = s"My line"

      parse(s"$line$lineDelimiter") mustBe Line(parsedLine = Option(line))
    }

    "be parsed from multiple lines" in {
      val line = s"My$lineDelimiter ${quote}line$quote complete$lineDelimiter"

      parse(s"$line$lineDelimiter") mustBe Line(parsedLine = Option("My line complete"))
    }

    "be pending parse" in {
      val line = s"My ${quote}line"

      parse(line) mustBe Line(accumulatedChars = List(line))
    }

    "be pending parse where new line would be embedded in quotes" in {
      val line = s"My ${quote}line$lineDelimiter"

      parse(line) mustBe Line(accumulatedChars = List(line))
    }
  }
}