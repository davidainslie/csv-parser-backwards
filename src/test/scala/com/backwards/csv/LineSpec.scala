package com.backwards.csv

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.tag._

class LineSpec extends AnyWordSpec with Matchers {
  "Line with default quote" should {
    val csvConfig = CsvConfig()
    import csvConfig._

    val conflate: (Line, String) => Line =
      Line.conflate(csvConfig)

    "be conflated from simple/single line" in {
      val line = s"My line$lineDelimiter"

      conflate(Line(), line) mustBe Line(line = line)
    }

    "be conflated from multiple lines" in {
      val line = s"My$lineDelimiter ${quote}line$quote complete$lineDelimiter"

      conflate(Line(), line) mustBe Line(line = line)
    }

    "be pending conflation" in {
      val line = s"My ${quote}line"

      conflate(Line(), line) mustBe Line(accumulatedChars = List(line))
    }

    "be pending conflation where new line would be embedded in quotes" in {
      val line = s"My ${quote}line$lineDelimiter"

      conflate(Line(), line) mustBe Line(accumulatedChars = List(line))
    }
  }

  "Line with overriding quote" should {
    val csvConfig = CsvConfig(quote = "#".tag[Quote])
    import csvConfig._

    val conflate: (Line, String) => Line =
      Line.conflate(csvConfig)

    "be conflated from simple/single line" in {
      val line = s"My line$lineDelimiter"

      conflate(Line(), line) mustBe Line(line = line)
    }

    "be conflated from multiple lines" in {
      val line = s"My$lineDelimiter ${quote}line$quote complete$lineDelimiter"

      conflate(Line(), line) mustBe Line(line = line)
    }

    "be pending conflation" in {
      val line = s"My ${quote}line"

      conflate(Line(), line) mustBe Line(accumulatedChars = List(line))
    }

    "be pending conflation where new line would be embedded in quotes" in {
      val line = s"My ${quote}line$lineDelimiter"

      conflate(Line(), line) mustBe Line(accumulatedChars = List(line))
    }
  }
}