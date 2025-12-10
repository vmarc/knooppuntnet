package kpn.core.tools.next.support

import kpn.api.custom.Timestamp
import kpn.core.analysis.TagInterpreter
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.config.Dirs
import org.apache.commons.io.FileUtils

import java.io.File
import scala.xml.XML

object NextCollectRouteRelationIdsTool {
  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val tool = new NextCollectRouteRelationIdsTool(overpassQueryExecutor)
    tool.collect()
  }
}

class NextCollectRouteRelationIdsTool(overpassQueryExecutor: OverpassQueryExecutor) {
  def collect(): Unit = {
    val routeIds = (collectIds("route").toSet ++ collectIds("superroute").toSet).toSeq.sorted
    val file = new File(Dirs.root, "next/all-route-ids.txt")
    FileUtils.writeStringToFile(file, routeIds.mkString("\n"), "UTF-8")
    println(s"done")
  }

  private def collectIds(typeValue: String): Seq[String] = {
    println(s"Collect all $typeValue ids")
    val meta = s"""[date:"${Timestamp.analysisStart.iso}"][timeout:1500][maxsize:24000000000]"""
    val routeTagValues = s"""[~"^route$$"~".*(${TagInterpreter.routeTagValues.mkString("|")}).*"]"""
    val query = s"""$meta;relation["type"="$typeValue"]$routeTagValues;out ids;"""
    val xmlString = overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    (xml.head \ "relation").map { relationElem =>
      (relationElem \ "@id").text
    }
  }
}
