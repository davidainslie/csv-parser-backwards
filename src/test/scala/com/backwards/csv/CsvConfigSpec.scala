package com.backwards.csv

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.tag._

class CsvConfigSpec extends AnyWordSpec with Matchers {
  "CSV config" should {
    "only be constructed with correctly tagged types" in {
      val csvConfig = CsvConfig(
        false.tag[Header],
        "\"".tag[Quote],
        ",".tag[FieldDelimiter],
        "\n".tag[LineDelimiter]
      )

      csvConfig mustBe a[CsvConfig]

      """
      CsvConfig(
        false,
        "\"".tag[Quote],
        ",".tag[FieldDelimiter],
        "\n".tag[LineDelimiter]
      )""" mustNot compile

      """
      CsvConfig(
        false.tag[Header],
        "\"",
        ",".tag[FieldDelimiter],
        "\n".tag[LineDelimiter]
      )""" mustNot compile

      """
      CsvConfig(
        false.tag[Header],
        "\"".tag[Quote],
        ",",
        "\n".tag[LineDelimiter]
      )""" mustNot compile

      """
      CsvConfig(
        false.tag[Header],
        "\"".tag[Quote],
        ",".tag[FieldDelimiter],
        "\n"
      )""" mustNot compile
    }
  }
}