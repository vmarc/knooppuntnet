package kpn.core.util

// based on: https://rosettacode.org/wiki/Natural_sorting#Scala

object NaturalSorting {

  def sort(input: Seq[String]): Seq[String] = {
    sortBy(input)(identity)
  }

  def sortBy[T](input: Seq[T])(convertToStringFunction: T => String): Seq[T] = {
    val unsorted = input.zip(input.map(convertToStringFunction).map(natural))
    unsorted.sortWith((x, y) => compareSplittedStrings(x._2, y._2) < 0).map(_._1)
  }

  private val INT = "([0-9]+)".r

  private def compareSplittedStrings(a: Array[String], b: Array[String]): Int = {
    val l = Math.min(a.length, b.length)
    (0 until l).segmentLength(i => a(i) equals b(i)) match {
      case i if i == l => Math.signum(b.length.toFloat - a.length.toFloat).toInt
      case i => (a(i), b(i)) match {
        case (INT(c), INT(d)) => Math.signum(c.toInt.toFloat - d.toInt.toFloat).toInt
        case (c, d) => c compareTo d
      }
    }
  }

  private def natural(s: String): Array[String] = {
    val replacements = Map('\u00df' -> "ss", '\u017f' -> "s", '\u0292' -> "s").withDefault(s => s.toString) // 8
    import java.text.Normalizer
    Normalizer.normalize(
      Normalizer.normalize(
        s.trim.toLowerCase, // 1.1, 1.2, 3
        Normalizer.Form.NFKC
      ), // 7
      Normalizer.Form.NFD
    ).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // 6
      .replaceAll("^(the|a|an) ", "") // 5
      .flatMap(replacements.apply) // 8
      .split(s"\\s+|(?=[0-9])(?<=[^0-9])|(?=[^0-9])(?<=[0-9])") // 1.3, 2 and 4
  }
}
