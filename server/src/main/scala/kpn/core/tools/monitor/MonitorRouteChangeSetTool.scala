package kpn.core.tools.monitor

import kpn.api.common.ReplicationId
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset
import scala.xml.XML

object MonitorRouteChangeSetTool {
  def main(args: Array[String]): Unit = {
    val executor = new OverpassQueryExecutorImpl()
    new MonitorRouteChangeSetTool(executor).analyze()
  }
}

class MonitorRouteChangeSetTool(overpassQueryExecutor: OverpassQueryExecutor) {

  private val routeId = 3121667L
  private val log = Log(classOf[MonitorRouteChangeSetTool])
  private val osmChangeRepository = new OsmChangeRepository(new File("/kpn/replicate"))

  var wayIds: Set[Long] = Set()
  var nodeIds: Set[Long] = Set()

  def analyze(): Unit = {
    val begin = ReplicationId(4, 131, 462)
    val end = ReplicationId(4, 323, 415)

    val timestamp = osmChangeRepository.timestamp(begin)
    val xmlString = writeXml(s"/kpn/wrk/begin/$routeId.xml", timestamp)
    updateNodeAndWayIds(xmlString)

    ReplicationId.range(begin, end) foreach { replicationId =>
      Log.context(s"${replicationId.name}") {
        val osmChange = osmChangeRepository.get(replicationId)
        val timestamp = osmChangeRepository.timestamp(replicationId)
        log.info(timestamp.yyyymmddhhmmss)

        val changeSets = ChangeSetBuilder.from(timestamp, osmChange)
        changeSets.foreach { changeSet =>
          val impacted = changeSet.changes.exists { change =>
            change.elements.exists {
              case node: RawNode => nodeIds.contains(node.id)
              case way: RawWay => wayIds.contains(way.id)
              case relation: RawRelation => relation.id == routeId
              case _ => false
            }
          }
          if (impacted) {
            log.info("Writing route files")
            val context = ChangeSetContext(replicationId, changeSet)
            val dir = s"/kpn/wrk/${changeSet.id}"
            new File(dir).mkdir()
            writeXml(s"$dir/$routeId-before.xml", context.timestampBefore)
            val xmlString = writeXml(s"$dir/$routeId-after.xml", context.timestampAfter)
            updateNodeAndWayIds(xmlString)
          }
        }
      }
    }
    log.info("Done")
  }

  private def writeXml(filename: String, timestamp: Timestamp): String = {
    val xml = overpassQueryExecutor.executeQuery(Some(timestamp), QueryRelation(routeId))
    FileUtils.writeStringToFile(new File(filename), xml, Charset.forName("UTF-8"))
    xml
  }

  private def updateNodeAndWayIds(xmlString: String): Unit = {
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val relation = new DataBuilder(rawData).data.relations(routeId)
    wayIds = relation.wayMembers.map(_.way.id).toSet
    nodeIds = relation.wayMembers.flatMap(_.way.nodes.map(_.id)).toSet
  }

}
