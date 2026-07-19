package kpn.tools.code.typescript

import scala.annotation.tailrec

/**
 * Converts camelCase strings to dashed-lowercase format.
 */
object CamelCaseUtil {
  def toDashed(input: String): String = {
    @tailrec
    def processCharacters(processed: List[Char], remaining: List[Char]): List[Char] = remaining match {
      case Nil => processed
      case a :: b :: c :: tail if isAcronymFollowedByLowercase(a, b, c) =>
        processCharacters(processed ++ List(a, '-', b, c), tail)
      case a :: b :: tail if isLowerFollowedByUpper(a, b) =>
        processCharacters(processed ++ List(a, '-', b), tail)
      case a :: tail =>
        processCharacters(processed :+ a, tail)
    }

    def isAcronymFollowedByLowercase(a: Char, b: Char, c: Char): Boolean =
      a.isUpper && b.isUpper && c.isLower

    def isLowerFollowedByUpper(a: Char, b: Char): Boolean =
      a.isLower && b.isUpper

    processCharacters(Nil, input.toList).mkString.toLowerCase
  }

  def toEnumValue(input: String): String = {
    @tailrec
    def processCharacters(processed: List[Char], remaining: List[Char]): List[Char] = remaining match {
      case Nil => processed
      case a :: b :: c :: tail if isAcronymFollowedByLowercase(a, b, c) =>
        processCharacters(processed ++ List(a, '_', b, c), tail)
      case a :: b :: tail if isLowerFollowedByUpper(a, b) =>
        processCharacters(processed ++ List(a, '_', b), tail)
      case a :: tail =>
        processCharacters(processed :+ a, tail)
    }

    def isAcronymFollowedByLowercase(a: Char, b: Char, c: Char): Boolean =
      a.isUpper && b.isUpper && c.isLower

    def isLowerFollowedByUpper(a: Char, b: Char): Boolean =
      a.isLower && b.isUpper

    processCharacters(Nil, input.toList).mkString.toUpperCase
  }
}
