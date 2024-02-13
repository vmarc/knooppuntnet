package kpn.core.tools.monitor.support

import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.server.analyzer.engine.monitor.structure.StructureElementAnalyzer
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.xml.XML

object StructureAnalysisTool {
  def main(args: Array[String]): Unit = {
    new StructureAnalysisTool().analyze()
  }
}

class StructureAnalysisTool {

  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()

  def analyze(): Unit = {
    val ids = FileUtils.readLines(new File("/kpn/cycling-nok-routes.txt"), Charset.forName("UTF-8")).asScala
    ids.zipWithIndex.foreach { case (id, index) =>
      val relationId = id.toLong
      val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelation(relationId))
      val xml = XML.loadString(xmlString)
      val rawData = new Parser().parse(xml)
      val relation = new DataBuilder(rawData).data.relations(relationId)
      val elementGroups = StructureElementAnalyzer.analyze(relation.members)
      println(s"${index + 1}/${ids.size} $relationId ${if (elementGroups.size > 1) "ISSUE" else "OK"}")
    }
  }
}
