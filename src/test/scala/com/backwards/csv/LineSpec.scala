package com.backwards.csv

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class LineSpec extends AnyWordSpec with Matchers {
  "Line with default quote" should {
    val quote = Quote().value

    val conflate: (Lines, String) => Lines =
      Line.conflate()

    "be conflated from single line" in {
      val line = "My line"

      conflate(Lines(), line) mustBe Lines(line = line)
    }

    "be conflated from multiple lines" in {
      val line = s"My\n ${quote}line$quote complete"

      conflate(Lines(), line) mustBe Lines(line = line)
    }

    "be pending conflation" in {
      val line = s"My ${quote}line"

      conflate(Lines(), line) mustBe Lines(accumulatedLines = List(line))
    }
  }

  "Line with overriding quote" should {
    val quote = Quote("#")

    val conflate: (Lines, String) => Lines =
      Line.conflate(quote)

    "be conflated from single line" in {
      val line = "My line"

      conflate(Lines(), line) mustBe Lines(line = line)
    }

    "be conflated from multiple lines" in {
      val line = s"My\n ${quote}line$quote complete"

      conflate(Lines(), line) mustBe Lines(line = line)
    }

    "be pending conflation" in {
      val line = s"My ${quote}line"

      conflate(Lines(), line) mustBe Lines(accumulatedLines = List(line))
    }
  }
}