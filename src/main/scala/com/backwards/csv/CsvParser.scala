package com.backwards.csv

import java.lang.System.lineSeparator
import scala.util.matching.Regex
import scala.util.parsing.combinator._
import cats.implicits._

class CsvParser(val csvConfig: CsvConfig) extends RegexParsers {
  override val skipWhitespace = false

  def quote: String = csvConfig.quote

  def delimiter: String = csvConfig.fieldDelimiter

  def crlf: Parser[String] = lineSeparator

  def text: Regex = "[^%s%s%s]".format(quote, delimiter, lineSeparator).r

  def unquotedField: Parser[String] = rep(text) ^^ (_.mkString)

  def quotedField: Parser[String] =
    quote ~> rep(text | delimiter | crlf) <~ quote ^^ (_.mkString)

  def field: Parser[String] = opt(unquotedField) ~ opt(quotedField) ~ opt(unquotedField) ^^ {
    case o1 ~ o2 ~ o3 => o1.getOrElse("") + o2.getOrElse("") + o3.getOrElse("")
  }

  def line: Parser[List[String]] = repsep(field, delimiter) <~ crlf

  def csv: Parser[List[List[String]]] = rep(line)

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