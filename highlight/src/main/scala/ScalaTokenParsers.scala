package lyrical.highlighter

import scala.util.parsing.combinator._

// これがscalaのParser Combinator
object ScalaTokenParsers extends RegexParsers {
  // RegexParsersが空白を無視しない
  override def skipWhitespace = false

  val eof = "".r
  val anyChar = "(.|\n)".r

  def scalaParser: Parser[String] = rep(tokenParser) ^^ { xs => xs.mkString }

  def tokenParser: Parser[String] = commentParser | keywordParser | rawParser
  
  def rawParser: Parser[String] = anyChar 

  def commentParser: Parser[String] = lineCommentParser | blockCommentParser

  val lineComment = "//.*\n".r
  def lineCommentParser: Parser[String] = lineComment ^^ {
    x => "<span class=\"comment\">" + x.takeWhile(_ != '\n').mkString + "</span><br>"
  }

  val blockComment = """/\*.*?\*/""".r
  def blockCommentParser: Parser[String] = blockComment ^^ {
    x => "<span class=\"comment\">" + x.replace("\n", "<br>") + "</span>"
  }

  val defKeyword = "def"
  val objectKeyword = "object"
  val classKeyword = "class"

  def keywordParser: Parser[String] = (defKeyword | objectKeyword | classKeyword) ^^ {
    x => """<span class="keyword">""" + x + "</span>"
  }

}
