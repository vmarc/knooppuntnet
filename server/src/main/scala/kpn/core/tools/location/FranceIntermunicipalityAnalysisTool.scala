package kpn.core.tools.location

import kpn.core.data.DataBuilder
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.core.overpass.QueryString
import kpn.core.tools.location.FranceIntermunicipalityAnalysisTool.intermunicipalitiesDir
import kpn.core.tools.location.FranceIntermunicipalityAnalysisTool.rootDir
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory

import java.io.File
import scala.xml.XML

object FranceIntermunicipalityAnalysisTool {

  private val rootDir = "/kpn/locations"
  private val intermunicipalitiesDir = rootDir + "/fr-intermunicipalities"

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
    FranceIntermunicipalities.types.foreach { intermunicipalityType =>
      val ids = loadIds(intermunicipalityType)
      ids.zipWithIndex.foreach { case (relationId, index) =>
        val rawData = OsmDataXmlReader.read(s"$intermunicipalitiesDir/$relationId.xml")
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
    FranceIntermunicipalities.types.foreach(loadOverpassIds)
  }

  private def loadOverpassIds(intermunicipalityType: String): Unit = {
    val filename = s"$intermunicipalitiesDir/fr-${intermunicipalityType.toLowerCase}.xml"
    val queryString = s"relation['boundary'~'local_authority|administrative']['local_authority:FR'='$intermunicipalityType'];out ids;"
    val xml = overpassQueryExecutor.executeQuery(None, QueryString(s"location", queryString))
    FileUtils.writeStringToFile(new File(filename), xml, "UTF-8")
  }

  def loadRelations(): Unit = {
    val ids = loadIds()
    ids.zipWithIndex.foreach { case (relationId, index) =>
      val filename = s"$intermunicipalitiesDir/$relationId.xml"
      println(s"${index + 1}/${ids.size} $filename")
      val xml = overpassQueryExecutor.executeQuery(None, QueryRelation(relationId))
      FileUtils.writeStringToFile(new File(filename), xml, "UTF-8")
    }
  }

  private def loadIds(): Seq[Long] = {
    FranceIntermunicipalities.types.flatMap(loadIds)
  }

  private def loadIds(intermunicipalityType: String): Seq[Long] = {
    val filename = s"$intermunicipalitiesDir/fr-${intermunicipalityType.toLowerCase}.xml"
    val xmlString = FileUtils.readFileToString(new File(filename), "UTF-8")
    val xml = XML.loadString(xmlString)
    (xml \ "relation").map { n => (n \ "@id").text.toLong }.distinct.sorted
  }

  private def loadIntermunicipalities(): Seq[IntermunicipalityGeometry] = {
    FranceIntermunicipalities.types.flatMap { intermunicipalityType =>
      val ids = loadIds(intermunicipalityType)
      ids.zipWithIndex.map { case (relationId, index) =>
        println(s"load $intermunicipalityType ${index + 1}/${ids.size} relation=$relationId")
        loadIntermunicipality(intermunicipalityType, relationId)
      }
    }
  }

  private def loadIntermunicipality(intermunicipalityType: String, relationId: Long): IntermunicipalityGeometry = {
    val rawData = OsmDataXmlReader.read(s"$intermunicipalitiesDir/$relationId.xml")
    val data = new DataBuilder(rawData).data
    val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", intermunicipalityType))
    if (locationRelations.size != 1) {
      throw new RuntimeException(s"Unexpected number of location relations in $relationId.xml: ${locationRelations.size}")
    }
    val relation = locationRelations.head
    val polygons = RelationPolygonBuilder.toPolygons(data, relation)
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
