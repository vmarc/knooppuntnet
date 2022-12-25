package kpn.server.api.monitor.route

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import kpn.core.overpass.QueryRelationStructure
import kpn.core.overpass.QueryRelationTopLevel
import kpn.server.api.monitor.MonitorRelationDataBuilder
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorRouteRelationRepository(
  overpassQueryExecutor: OverpassQueryExecutor,
) {

  def load(timestamp: Option[Timestamp], relationId: Long): Option[Relation] = {
    val xmlString = overpassQueryExecutor.executeQuery(timestamp, QueryRelation(relationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(relationId)
  }

  def loadStructure(timestamp: Option[Timestamp], relationId: Long): Option[Relation] = {
    val xmlString = overpassQueryExecutor.executeQuery(timestamp, QueryRelationStructure(relationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new MonitorRelationDataBuilder(rawData).data
    data.relations.get(relationId)
  }

  def loadTopLevel(timestamp: Option[Timestamp], relationId: Long): Option[Relation] = {
    val xmlString = overpassQueryExecutor.executeQuery(timestamp, QueryRelationTopLevel(relationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(relationId)
  }
}
