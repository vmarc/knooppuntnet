package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.data.raw.RawData
import kpn.core.loadOld.OsmDataXmlWriter
import kpn.core.loadOld.Parser

import java.io.File
import java.io.InputStream
import java.io.PrintWriter
import scala.xml.XML

object CaseStudyCleanTool {
  def main(args: Array[String]): Unit = {
    new CaseStudyCleanTool().clean("monitor/6968141")
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
