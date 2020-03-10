package com.backwards.csv

import java.lang.System.lineSeparator
import scala.util.matching.Regex
import scala.util.parsing.combinator._
import cats.implicits._

class CsvParser(val csvConfig: CsvConfig) extends RegexParsers {
  override val skipWhitespace = false

  val quote: String = csvConfig.quote

  val delimiter: String = csvConfig.fieldDelimiter

  val crlf: Parser[String] = lineSeparator

  val text: Regex = "[^%s%s%s]".format(quote, delimiter, lineSeparator).r

  val unquotedField: Parser[String] = rep(text) ^^ (_.mkString)

  val quotedField: Parser[String] =
    quote ~> rep(text | delimiter | crlf) <~ quote ^^ (_.mkString)

  val field: Parser[String] = quotedField | unquotedField

  val line: Parser[List[String]] = repsep(field, delimiter) <~ crlf

  val csv: Parser[List[List[String]]] = rep(line)

  def parse(line: String): String Either List[List[String]] = {
    if (line.endsWith(lineSeparator))
      parseAll(csv, line)
    else
      parseAll(csv, line + lineSeparator)
  } match {
    case Success(result, _) => result.asRight[String]
    case Failure(message, _) => message.asLeft[List[List[String]]]
    case Error(message, _) => message.asLeft[List[List[String]]]
  }
}