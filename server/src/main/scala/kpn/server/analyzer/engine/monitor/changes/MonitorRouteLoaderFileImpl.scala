package kpn.server.analyzer.engine.monitor.changes

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset
import scala.xml.XML

class MonitorRouteLoaderFileImpl extends MonitorRouteLoader {

  def loadInitial(timestamp: Timestamp, routeId: Long): Option[Relation] = {
    load(s"/kpn/wrk/begin/$routeId.xml", routeId)
  }

  def loadBefore(changeSetId: Long, timestamp: Timestamp, routeId: Long): Option[Relation] = {
    load(s"/kpn/wrk/$changeSetId/$routeId-before.xml", routeId)
  }

  def loadAfter(changeSetId: Long, timestamp: Timestamp, routeId: Long): Option[Relation] = {
    load(s"/kpn/wrk/$changeSetId/$routeId-after.xml", routeId)
  }

  private def load(filename: String, routeId: Long): Option[Relation] = {
    val xmlString = FileUtils.readFileToString(new File(filename), Charset.forName("UTF-8"))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    Some(new DataBuilder(rawData).data.relations(routeId))
  }

}
