package kpn.server.overpass

import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryFullRelations
import kpn.core.overpass.QueryRelations
import kpn.core.overpass.QueryRouteIds
import kpn.core.util.Log
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class OverpassRepositoryImpl(
  nonCachingOverpassQueryExecutor: OverpassQueryExecutor
) extends OverpassRepository {

  private val log = Log(classOf[OverpassRepositoryImpl])

  def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation] = {
    val rawData = relationsQuery(timestamp, QueryRelations(relationIds), relationIds)
    rawData.relations
  }

  def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation] = {
    val rawData = relationsQuery(timestamp, QueryFullRelations(relationIds), relationIds)
    val data = new DataBuilder(rawData).data
    relationIds.map { routeId =>
      data.relations(routeId)
    }
  }

  override def routeIds(timestamp: Timestamp): Seq[Long] = {
    val overpassQuery = QueryRouteIds()
    ids(timestamp, "relation", overpassQuery)
  }

  private def ids(timestamp: Timestamp, elementTag: String, query: OverpassQuery): Seq[Long] = {
    parseIds(elementTag, nonCachingOverpassQueryExecutor.executeQuery(Some(timestamp), query))
  }

  private def parseIds(elementTag: String, xmlString: String): Seq[Long] = {
    val xml = try {
      XML.loadString(xmlString)
    }
    catch {
      case e: SAXParseException => throw new RuntimeException(s"Could not load xml\n$xmlString", e)
    }
    (xml \ elementTag).map { n => (n \ "@id").text.toLong }.distinct.sorted
  }

  private def relationsQuery(timestamp: Timestamp, query: OverpassQuery, relationIds: Seq[Long]): RawData = {
    if (relationIds.isEmpty) {
      RawData()
    }
    else {
      val xmlString: String = log.infoElapsed {
        val xml = nonCachingOverpassQueryExecutor.executeQuery(Some(timestamp), query)
        (s"Load ${relationIds.size} relations at ${timestamp.iso}", xml)
      }

      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: Exception =>
          throw new RuntimeException(s"Could not load relations at ${timestamp.iso}", e)
      }

      new Parser().parse(xml.head)
    }
  }

}
