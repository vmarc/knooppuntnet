package kpn.core.tools.monitor.support

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerHelper
import kpn.server.analyzer.engine.changes.OsmChangeRepositoryImpl
import kpn.server.analyzer.engine.context.ElementIdMap
import kpn.server.analyzer.engine.monitor.changes.MonitorChangeImpactAnalyzerImpl
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset
import scala.xml.XML

object MonitorRouteCreateXmlsTool {

  val routeIds: Seq[Long] = Seq(
    3121667L,
    3121668L,
    5951316L,
    2907324L,
    2923308L,
    13658L,
    2629186L,
    133437L,
    13638L,
    6481535L,
    5556328L,
    3194117L,
    2929186L,
    8613893L,
    197843L,
    2067765L
  )

  def main(args: Array[String]): Unit = {
    val executor = new OverpassQueryExecutorImpl()
    new MonitorRouteCreateXmlsTool(executor, routeIds).analyze()
  }
}

class MonitorRouteCreateXmlsTool(overpassQueryExecutor: OverpassQueryExecutor, routeIds: Seq[Long]) {

  private val elementIdMap = ElementIdMap()
  private val monitorChangeImpactAnalyzer = new MonitorChangeImpactAnalyzerImpl()
  private val osmChangeRepository = new OsmChangeRepositoryImpl(new File("/kpn/replicate"))
  private val log = Log(classOf[MonitorRouteCreateXmlsTool])

  def analyze(): Unit = {

    val begin = ReplicationId(4, 131, 462)
    val end = ReplicationId(4, 340, 620)

    initialLoad(osmChangeRepository.timestamp(begin))

    ReplicationId.range(begin, end).foreach { replicationId =>
      Log.context(s"${replicationId.name}") {
        processReplication(replicationId)
      }
    }
    log.info("Done")
  }

  private def processReplication(replicationId: ReplicationId): Unit = {

    val osmChange = osmChangeRepository.get(replicationId)
    val timestamp = osmChangeRepository.timestamp(replicationId)
    log.info(timestamp.yyyymmddhhmmss)

    val changeSets = ChangeSetBuilder.from(timestamp, osmChange)
    changeSets.foreach { changeSet =>
      elementIdMap.foreach { (routeId, elementIds) =>
        if (monitorChangeImpactAnalyzer.hasImpact(changeSet, routeId, elementIds)) {
          val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
          val context = ChangeSetContext(
            replicationId,
            changeSet,
            elementIds
          )
          val dir = s"/kpn/wrk/${changeSet.id}"
          new File(dir).mkdir()
          writeXml(routeId, s"$dir/$routeId-before.xml", context.timestampBefore)
          val xmlString = writeXml(routeId, s"$dir/$routeId-after.xml", context.timestampAfter)
          updateElementIdMap(routeId, xmlString)
        }
      }
    }
  }

  private def initialLoad(timestamp: Timestamp): Unit = {
    routeIds.foreach { routeId =>
      val xmlString = writeXml(routeId, s"/kpn/wrk/begin/$routeId.xml", timestamp)
      updateElementIdMap(routeId, xmlString)
    }
  }

  private def writeXml(routeId: Long, filename: String, timestamp: Timestamp): String = {
    log.info(filename)
    val xml = overpassQueryExecutor.executeQuery(Some(timestamp), QueryRelation(routeId))
    FileUtils.writeStringToFile(new File(filename), xml, Charset.forName("UTF-8"))
    xml
  }

  private def updateElementIdMap(routeId: Long, xmlString: String): Unit = {
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val routeRelation = new DataBuilder(rawData).data.relations(routeId)
    val elementIds = RelationAnalyzerHelper.toElementIds(routeRelation)
    elementIdMap.add(routeId, elementIds)
  }
}
