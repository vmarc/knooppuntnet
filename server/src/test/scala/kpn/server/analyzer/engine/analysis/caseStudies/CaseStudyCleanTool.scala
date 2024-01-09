package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.data.raw.RawData
import kpn.core.loadOld.OsmDataXmlWriter
import kpn.core.loadOld.Parser

import java.io.File
import java.io.InputStream
import java.io.PrintWriter
import scala.xml.InputSource
import scala.xml.XML

object CaseStudyCleanTool {
  def main(args: Array[String]): Unit = {
    val ids = Seq(
      "10015252",
      "1014751",
      "1029885",
      "1029893",
      "10460202",
      "10495912",
      "10779",
      "10887712",
      "10993501",
      "11047960",
      "11109600",
      "11512870",
      "11512871",
      "11527464-adapted",
      "11527464",
      "11659448",
      "11721562-adapted",
      "11721562",
      "11771769",
      "11772920",
      "11829059",
      "11829061",
      "11829926",
      "11838989",
      "11858847",
      "1193198",
      "119410",
      "12219285",
      "12280062",
      "12410463",
      "1245740",
      "12713351-after",
      "12713351-before",
      "12859714",
      "12863114",
      "12864030",
      "12865974",
      "12867780",
      "12882087",
      "12882250",
      "12887722",
      "12921288",
      "12921427",
      "12955973",
      "12964641",
      "12966181",
      "12966645",
      "12966812",
      "13014492",
      "13014677",
      "13014771",
      "13305500",
      "13328443",
      "13331398",
      "13504960",
      "13508056",
      "13508061",
      "13519504",
      "13569497",
      "13619463",
      "13626627",
      "13669113",
      "145281",
      "15656714",
      "15656715",
      "15656716",
      "1672723",
      "172129",
      "18984",
      "19227",
      "222560",
      "2614657",
      "28182",
      "28184",
      "2946783",
      "3119024",
      "312993",
      "3330377",
      "3431904",
      "3683944",
      "3715798",
      "3792006",
      "4271",
      "535487",
      "6635664",
      "6635670",
      "7175609",
      "7328339",
      "7645863",
      "7776398",
      "8473146",
      "9174227",
      "9432838",
      "9499242",
      "9515132",
      "9637368",
      "load-routes",
      "minute-diff-5719419",
      "network-2243640",
      "network-4257206",
      "network-8438300-after",
      "network-8438300-before",
      "node-2969204425-after",
      "node-2969204425-before",
      "RonGpsBackward",
      "RonGpsFixed",
      "RonGpsOriginal",
      "route-3945543",
    )
    ids.foreach(id => new CaseStudyCleanTool().clean(id))
  }
}

class CaseStudyCleanTool {

  private val root = "/home/vmarc/wrk/projects/knooppuntnet/server/src/test/resources"

  def clean(name: String): Unit = {
    val filename = s"$root/case-studies/$name.xml"
    val data = readFile(filename)
    writeFile(data, filename)
  }

  private def readFile(filename: String): RawData = {
    val xml = XML.loadFile(filename)
    new Parser(full = false).parse(xml)
  }

  private def writeFile(data: RawData, filename: String): Unit = {
    val fileWriter = new PrintWriter(new File(filename))
    val printWriter = new PrintWriter(fileWriter)
    new OsmDataXmlWriter(data, printWriter, full = false).print()
    printWriter.close()
  }

  private def skipFirstLine(stream: InputStream): Unit = {
    var done = false
    do {
      val c = stream.read()
      if (c == '\n') {
        done = true
      }
    } while (done == false)
  }
}
