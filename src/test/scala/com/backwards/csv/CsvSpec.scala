package com.backwards.csv

import java.io.File
import cats.implicits._
import org.scalatest.EitherValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.csv.ArgsParser.parse
import com.backwards.tag._

class CsvSpec extends AnyWordSpec with Matchers with EitherValues {
  "CSV longhand command line arguments" should {
    "be parsed upon providing all options" in {
      val file = new File("my-input.csv")
      val header = true.tag[Header]
      val quote = "'".tag[Quote]
      val fieldDelimiter = "|".tag[FieldDelimiter]
      val lineDelimiter = "\r\n".tag[LineDelimiter]

      val args = List(
        s"--csv=${file.getName}",
        s"--header=$header",
        s"--quote=$quote",
        s"--fieldDelimiter=$fieldDelimiter",
        s"--lineDelimiter=$lineDelimiter"
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
      val header = true.tag[Header]
      val quote = "'".tag[Quote]
      val fieldDelimiter = "|".tag[FieldDelimiter]
      val lineDelimiter = "\r\n".tag[LineDelimiter]

      val args = List(
        s"-c=${file.getName}",
        s"-h=$header",
        s"-q=$quote",
        s"-f=$fieldDelimiter",
        s"-l=$lineDelimiter"
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