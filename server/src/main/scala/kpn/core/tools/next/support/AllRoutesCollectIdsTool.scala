package kpn.core.tools.next.support

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.config.Dirs
import org.apache.commons.io.FileUtils

import java.io.File
import scala.xml.XML

object AllRoutesCollectIdsTool {
  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val tool = new AllRoutesCollectIdsTool(overpassQueryExecutor)
    tool.collect()
  }
}

class AllRoutesCollectIdsTool(overpassQueryExecutor: OverpassQueryExecutor) {
  def collect(): Unit = {
    val routeIds = (collect("route").toSet ++ collect("superroute").toSet).toSeq.sorted
    val file = new File(Dirs.root, "next/all-route-ids.txt")
    FileUtils.writeStringToFile(file, routeIds.mkString("\n"), "UTF-8")
    println(s"done")
  }

  private def collect(typeValue: String): Seq[String] = {
    println(s"Collect all $typeValue ids")
    val meta = s"""[date:"${Timestamp.analysisStart.iso}"][timeout:1500][maxsize:24000000000]"""
    val routeTagValues = s"""[~"^route$$"~".*(foot|hiking|walking|bicycle|horse|motorboat|canoe|inline_skates).*"]"""
    val query = s"""$meta;relation["type"="$typeValue"]$routeTagValues;out ids;"""
    val xmlString = overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    (xml.head \ "relation").map { relationElem =>
      (relationElem \ "@id").text
    }
  }
}
