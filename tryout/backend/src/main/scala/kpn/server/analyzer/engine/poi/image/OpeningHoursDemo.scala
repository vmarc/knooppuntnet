package kpn.server.analyzer.engine.poi.image

import ch.poole.openinghoursparser.OpeningHoursParseException
import ch.poole.openinghoursparser.OpeningHoursParser
import ch.poole.openinghoursparser.Util

import java.io.ByteArrayInputStream

object OpeningHoursDemo {
  def main(args: Array[String]): Unit = {

    val line = "Mo-Fr 05:00-20:00; Sa 07:00-20:00; PH,Su 07:00-19:00"
    try {
      val parser = new OpeningHoursParser(new ByteArrayInputStream(line.getBytes))
      val rules = parser.rules(false, false)
      rules.forEach(rule => println(rule.toString))

      println( Util.rulesToOpeningHoursString(rules))

      println(rules.size)
    } catch {
      case e: OpeningHoursParseException =>

      e.getExceptions.forEach{ex =>
        println(ex.getMessage)
      }
    }
  }
}
