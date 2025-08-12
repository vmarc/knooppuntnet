package kpn.server.overpass

import kpn.api.common.Relation
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.doc.RouteRelation
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryFullRelations
import kpn.core.overpass.QueryNetworkIds
import kpn.core.overpass.QueryNodeIds
import kpn.core.overpass.QueryNodes
import kpn.core.overpass.QueryRelationStructure
import kpn.core.overpass.QueryRelationTopLevel
import kpn.core.overpass.QueryRelations
import kpn.core.util.Log
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class OverpassRepositoryImpl(
  overpassQueryExecutor: OverpassQueryExecutor
) extends OverpassRepository {

  private val log = Log(classOf[OverpassRepositoryImpl])

  override def nodeIds(timestamp: Timestamp): Seq[Long] = {
    val query = QueryNodeIds()
    ids(timestamp, "node", query).distinct.sorted
  }

  override def networkIds(timestamp: Timestamp): Seq[Long] = {
    val query = QueryNetworkIds()
    ids(timestamp, "relation", query).distinct.sorted
  }

  override def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = {
    if (nodeIds.isEmpty) {
      Seq.empty
    }
    else {
      val query = QueryNodes("nodes", nodeIds)

      val xmlString: String = log.debugElapsed {
        val xml = overpassQueryExecutor.executeQuery(Some(timestamp), query)
        (s"Load ${nodeIds.size} nodes at ${timestamp.iso}", xml)
      }

      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: Exception =>
          throw new RuntimeException(s"Could not load nodes at ${timestamp.iso}", e)
      }

      val rawData = new Parser().parse(xml.head)
      rawData.nodes
    }
  }

  override def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation] = {
    val rawData = relationsQuery(timestamp, QueryRelations(relationIds), relationIds)
    rawData.relations
  }

  override def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation] = {
    val rawData = relationsQuery(timestamp, QueryFullRelations(relationIds), relationIds)
    val data = new DataBuilder(rawData).data
    relationIds.flatMap { routeId =>
      data.relations.get(routeId)
    }
  }

  override def relationTopLevel(timestamp: Timestamp, relationId: Long): Option[Relation] = {
    val query = QueryRelationTopLevel(relationId)
    val xmlString = overpassQueryExecutor.executeQuery(Some(timestamp), query)
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    new BaseRelationBuilder(rawData, log).build(relationId)
  }

  override def subRelationTree(timestamp: Timestamp, relationId: Long): Option[RouteRelation] = {
    val xmlString = overpassQueryExecutor.executeQuery(Some(timestamp), QueryRelationStructure(relationId))
    val filteredXmlString = xmlString.linesIterator.filter { line =>
      !(line.contains("<node id") || line.contains("<way id") || line.contains("<member type=\"node\"") || line.contains("<member type=\"way\""))
    }.mkString("\n")
    val xml = XML.loadString(filteredXmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(relationId).map { relation =>
      RouteRelation.from(relation, None)
    }
  }

  override def routeIds(timestamp: Timestamp, typeValue: String): Seq[Long] = {
    val meta = s"""[date:"${timestamp.iso}"][timeout:1500][maxsize:24000000000]"""
    val routeTagValues = s"""[~"^route$$"~".*(foot|hiking|walking|bicycle|horse|motorboat|canoe|inline_skates).*"]"""
    val query = s"""$meta;relation["type"="$typeValue"]$routeTagValues;out ids;"""
    val xmlString = overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    (xml.head \ "relation").map { relationElem =>
      (relationElem \ "@id").text.toLong
    }
  }

  private def ids(timestamp: Timestamp, elementTag: String, query: OverpassQuery): Seq[Long] = {
    parseIds(elementTag, overpassQueryExecutor.executeQuery(Some(timestamp), query))
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
      val xmlString: String = log.debugElapsed {
        val xml = overpassQueryExecutor.executeQuery(Some(timestamp), query)
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
