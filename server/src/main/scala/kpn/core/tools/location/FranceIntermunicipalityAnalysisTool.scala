package kpn.core.tools.location

import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.core.overpass.QueryString
import kpn.core.tools.country.PolygonBuilder
import kpn.core.tools.country.SkeletonData
import kpn.core.tools.country.SkeletonNode
import kpn.core.tools.country.SkeletonRelation
import kpn.core.tools.country.SkeletonWay
import kpn.core.tools.location.FranceIntermunicipalityAnalysisTool.intercommunalitiesDir
import kpn.core.tools.location.FranceIntermunicipalityAnalysisTool.intermunicipalityTypes
import kpn.core.tools.location.FranceIntermunicipalityAnalysisTool.rootDir
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

import java.io.File
import scala.xml.XML

object FranceIntermunicipalityAnalysisTool {

  private val rootDir = "/kpn/locations"
  private val intercommunalitiesDir = rootDir + "/fr-intercommunalities"
  private val intermunicipalityTypes = Seq("CA", "CC", "CU", "metropole")

  def main(args: Array[String]): Unit = {
    val tool = new FranceIntermunicipalityAnalysisTool()
    tool.loadOverpassIds()
    tool.loadRelations()
    tool.assertAllIntermunicipalitiesHaveSirenCode()
    tool.assertIntermunicipalitiesDoNotOverlap()
    tool.reportOrphanMunicipalities()
  }
}

class FranceIntermunicipalityAnalysisTool {

  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()

  case class IntermunicipalityGeometry(intermunicipalityType: String, relationId: Long, geometry: LocationGeometry)

  def reportOrphanMunicipalities(): Unit = {
    val france = LocationGeometry.load(s"$rootDir/fr/geometries/fr.json")
    val intermunicipalities = loadIntermunicipalities().filter(x => france.contains(x.geometry))
    val municipalities = loadMunicipalities()

    municipalities.zipWithIndex.foreach { case (municipality, index) =>
      if ((index % 1000) == 0) {
        println(s"$index/${municipalities.size}")
      }
      val locationGeometry = LocationGeometry(municipality.geometry)
      if (france.contains(locationGeometry)) {
        val insee = municipality.tags.getOrElse("ref:INSEE", "?")
        val relationId = Math.abs(municipality.locationJson.properties.osm_id)
        val overlapping = intermunicipalities.filter(x => x.geometry.overlap(locationGeometry) > 0.25)
        val summary = s"${relationId}L, // $insee  ${municipality.name}"
        if (overlapping.size < 1) {
          println(s"ORPHAN $summary")
        }
        else if (overlapping.size > 1) {
          val overlappingIntermunicipalities = overlapping.map(_.relationId).mkString(",")
          println(s"OVERLAP $summary, overlapping=$overlappingIntermunicipalities")
        }
      }
    }
  }

  def assertIntermunicipalitiesDoNotOverlap(): Unit = {
    val intermunicipalities = loadIntermunicipalities()
    val combinations = intermunicipalities.combinations(2).toSeq
    combinations.zipWithIndex.foreach { case (Seq(area1, area2), index) =>
      if ((index % 10000) == 0) {
        println(s"$index/${combinations.size}")
      }
      if (area1.geometry.overlap(area2.geometry) > 0.05) {
        println(s"OVERLAP ${area1.intermunicipalityType}-${area1.relationId} <> ${area2.intermunicipalityType}-${area2.relationId}")
      }
    }
  }

  def assertAllIntermunicipalitiesHaveSirenCode(): Unit = {
    intermunicipalityTypes.foreach { intermunicipalityType =>
      val ids = loadIds(intermunicipalityType)
      ids.zipWithIndex.foreach { case (relationId, index) =>
        val rawData = OsmDataXmlReader.read(s"$intercommunalitiesDir/$relationId.xml")
        val data = new DataBuilder(rawData).data
        val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", intermunicipalityType))
        if (locationRelations.size != 1) {
          throw new RuntimeException(s"Unexpected number of location relations in $relationId.xml: ${locationRelations.size}")
        }
        val relation = locationRelations.head
        relation.tags("name") match {
          case None => println(s"$relationId: name not found")
          case Some(name) =>
            relation.tags("ref:FR:SIREN") match {
              case None => println(s"$relationId: SIREN code not found")
              case Some(sirenCode) =>
                println(s"${index + 1}/${ids.size} relation=$relationId, id=$sirenCode, name=$name")
            }
        }
      }
      println(s"$intermunicipalityType ${ids.size} checked")
    }
  }

  def loadOverpassIds(): Unit = {
    intermunicipalityTypes.foreach(loadOverpassIds)
  }

  private def loadOverpassIds(intermunicipalityType: String): Unit = {
    val filename = s"$intercommunalitiesDir/fr-${intermunicipalityType.toLowerCase}.xml"
    val queryString = s"relation['boundary'~'local_authority|administrative']['local_authority:FR'='$intermunicipalityType'];out ids;"
    val xml = overpassQueryExecutor.executeQuery(None, QueryString(s"location", queryString))
    FileUtils.writeStringToFile(new File(filename), xml, "UTF-8")
  }

  def loadRelations(): Unit = {
    val ids = loadIds()
    ids.zipWithIndex.foreach { case (relationId, index) =>
      val filename = s"$intercommunalitiesDir/$relationId.xml"
      println(s"${index + 1}/${ids.size} $filename")
      val xml = overpassQueryExecutor.executeQuery(None, QueryRelation(relationId))
      FileUtils.writeStringToFile(new File(filename), xml, "UTF-8")
    }
  }

  private def loadIds(): Seq[Long] = {
    intermunicipalityTypes.flatMap(loadIds)
  }

  private def loadIds(intermunicipalityType: String): Seq[Long] = {
    val filename = s"$intercommunalitiesDir/fr-${intermunicipalityType.toLowerCase}.xml"
    val xmlString = FileUtils.readFileToString(new File(filename), "UTF-8")
    val xml = XML.loadString(xmlString)
    (xml \ "relation").map { n => (n \ "@id").text.toLong }.distinct.sorted
  }

  private def toPolygons(data: Data, relation: Relation): Seq[Polygon] = {

    val nodes = data.nodes.map { case (id, node) =>
      id -> SkeletonNode(node.id, node.lon, node.lat)
    }

    val ways = data.ways.map { case (id, way) =>
      id -> SkeletonWay(id, way.nodes.map(_.id))
    }

    val rels = data.raw.relations.map { relation =>
      relation.id -> SkeletonRelation(relation.id, relation.members)
    }.toMap

    val skeletonData = SkeletonData(
      relation.id,
      nodes,
      ways,
      rels
    )

    new PolygonBuilder("xx", skeletonData).polygons()
  }

  private def loadIntermunicipalities(): Seq[IntermunicipalityGeometry] = {
    intermunicipalityTypes.flatMap { intermunicipalityType =>
      val ids = loadIds(intermunicipalityType)
      ids.zipWithIndex.map { case (relationId, index) =>
        println(s"load $intermunicipalityType ${index + 1}/${ids.size} relation=$relationId")
        loadIntermunicipality(intermunicipalityType, relationId)
      }
    }
  }

  private def loadIntermunicipality(intermunicipalityType: String, relationId: Long): IntermunicipalityGeometry = {
    val rawData = OsmDataXmlReader.read(s"$intercommunalitiesDir/$relationId.xml")
    val data = new DataBuilder(rawData).data
    val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", intermunicipalityType))
    if (locationRelations.size != 1) {
      throw new RuntimeException(s"Unexpected number of location relations in $relationId.xml: ${locationRelations.size}")
    }
    val relation = locationRelations.head
    val polygons = toPolygons(data, relation)
    val geometry = if (polygons.size != 1) {
      polygons.head
    }
    else {
      val geomFactory = new GeometryFactory
      new GeometryCollection(polygons.toArray, geomFactory)
    }
    IntermunicipalityGeometry(
      intermunicipalityType,
      relationId,
      LocationGeometry(geometry)
    )
  }

  private def loadMunicipalities(): Seq[InterpretedLocationJson] = {
    InterpretedLocationJson.load(s"/kpn/locations/osm-boundaries-2021-11-01/fr-level-8.geojson.gz")
      .filter(_.tags.contains("ref:INSEE"))
      .sortBy(_.tags("ref:INSEE"))
  }
}
