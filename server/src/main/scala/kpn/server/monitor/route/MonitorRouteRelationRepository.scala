package kpn.server.monitor.route

import kpn.api.common.data.raw.RawData
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import kpn.core.overpass.QueryRelationStructure
import kpn.core.overpass.QueryRelationTopLevel
import kpn.server.monitor.MonitorRelationDataBuilder
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorRouteRelationRepository(
  overpassQueryExecutor: OverpassQueryExecutor,
) {

  def load(timestamp: Option[Timestamp], relationId: Long): Option[Relation] = {
    val rawData = execute(timestamp, QueryRelation(relationId))
    val data = new DataBuilder(rawData).data
    data.relations.get(relationId)
  }

  def loadStructure(timestamp: Option[Timestamp], relationId: Long): Option[Relation] = {
    val rawData = execute(timestamp, QueryRelationStructure(relationId))
    val data = new MonitorRelationDataBuilder(rawData).data
    data.relations.get(relationId)
  }

  def loadTopLevel(timestamp: Option[Timestamp], relationId: Long): Option[Relation] = {
    val rawData = execute(timestamp, QueryRelationTopLevel(relationId))
    val data = new RelationTopLevelDataBuilder(rawData, relationId).data
    data.relations.get(relationId)
  }

  private def execute(timestamp: Option[Timestamp], query: OverpassQuery): RawData = {
    val xmlString = overpassQueryExecutor.executeQuery(timestamp, query)
    val xml = XML.loadString(xmlString)
    new Parser().parse(xml.head)
  }
}