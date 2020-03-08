package com.backwards.csv

import java.io.File
import cats.implicits._
import org.scalatest.EitherValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.csv.ArgsParser.parse

class CsvSpec extends AnyWordSpec with Matchers with EitherValues {
  "CSV longhand command line arguments" should {
    "be parsed upon providing all options" in {
      val file = new File("my-input.csv")
      val header = Header(true)
      val quote = Quote("'")
      val fieldDelimiter = FieldDelimiter("|")
      val lineDelimiter = LineDelimiter("\r\n")

      val args = List(
        s"--csv=${file.getName}",
        s"--header=${header.value}",
        s"--quote=${quote.value}",
        s"--fieldDelimiter=${fieldDelimiter.value}",
        s"--lineDelimiter=${lineDelimiter.value}"
      )

      parse(args) mustBe Csv(file, CsvConfig(header, quote, fieldDelimiter, lineDelimiter)).some
    }

    "be parsed upon providing all mandatory options" in {
      val file = new File("my-input.csv")

      val args = List(s"--csv=${file.getName}")

      parse(args) mustBe Csv(file).some
    }
  }

  "CSV shorthand command line arguments" should {
    "be parsed upon providing all options" in {
      val file = new File("my-input.csv")
      val header = Header(true)
      val quote = Quote("'")
      val fieldDelimiter = FieldDelimiter("|")
      val lineDelimiter = LineDelimiter("\r\n")

      val args = List(
        s"-c=${file.getName}",
        s"-h=${header.value}",
        s"-q=${quote.value}",
        s"-f=${fieldDelimiter.value}",
        s"-l=${lineDelimiter.value}"
      )

      parse(args) mustBe Csv(file, CsvConfig(header, quote, fieldDelimiter, lineDelimiter)).some
    }

    "be parsed upon providing all mandatory options" in {
      val file = new File("my-input.csv")

      val args = List(s"-c=${file.getName}")

      parse(args) mustBe Csv(file).some
    }
  }

  "CSV invalid command line arguments" should {
    "fail parsing upon missing mandatory options" in {
      parse(Nil) mustBe None
    }
  }
}