package com.backwards.csv

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.csv.Line._
import com.backwards.tag._

class LineSpec extends AnyWordSpec with Matchers {
  "Line with default quote" should {
    val csvParser = new CsvParser(CsvConfig())

    import csvParser.csvConfig._

    val parse: (Line, String) => Line =
      Line.parse(csvParser)

    "be parsed from simple/single line" in {
      val line = s"My line"

      parse(excludesHeader, s"$line$lineDelimiter") mustBe excludesHeader(Option(line))
    }

    "be parsed from multiple lines" in {
      val line = s"My$lineDelimiter ${quote}line$quote complete"

      parse(excludesHeader, s"$line$lineDelimiter") mustBe excludesHeader(Option("My line complete"))
    }

    "be pending parse" in {
      val line = s"My ${quote}line"

      parse(excludesHeader, line) mustBe excludesHeader(accumulatedChars = List(line))
    }

    "be pending parse where new line would be embedded in quotes" in {
      val line = s"My ${quote}line$lineDelimiter"

      parse(excludesHeader, line) mustBe excludesHeader(accumulatedChars = List(line))
    }
  }

  "Line with overriding quote" should {
    val csvParser = new CsvParser(CsvConfig(quote = "#".tag[Quote]))

    import csvParser.csvConfig._

    val parse: (Line, String) => Line =
      Line.parse(csvParser)

    "be parsed from simple/single line" in {
      val line = s"My line"

      parse(excludesHeader, s"$line$lineDelimiter") mustBe excludesHeader(Option(line))
    }

    "be parsed from multiple lines" in {
      val line = s"My$lineDelimiter ${quote}line$quote complete$lineDelimiter"

      parse(excludesHeader, s"$line$lineDelimiter") mustBe excludesHeader(Option("My line complete"))
    }

    "be pending parse" in {
      val line = s"My ${quote}line"

      parse(excludesHeader, line) mustBe excludesHeader(accumulatedChars = List(line))
    }

    "be pending parse where new line would be embedded in quotes" in {
      val line = s"My ${quote}line$lineDelimiter"

      parse(excludesHeader, line) mustBe excludesHeader(accumulatedChars = List(line))
    }
  }
}