package kpn.core.tools.location

import kpn.api.custom.Country
import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.core.tools.country.PolygonBuilder
import kpn.core.tools.country.SkeletonData
import kpn.core.tools.country.SkeletonNode
import kpn.core.tools.country.SkeletonRelation
import kpn.core.tools.country.SkeletonWay
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationDefinition
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.LocationDefinition
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader
import org.apache.commons.io.FileUtils
import org.geotools.geometry.jts.GeometryClipper
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

import java.awt.Color
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.xml.XML

object LocationExplorerTool {
  def main(args: Array[String]): Unit = {
    val tool = new LocationExplorerTool(
      new OverpassQueryExecutorRemoteImpl()
    )
    // tool.drawExistingConfigurationImages()
    //tool.exploreCC()
    // tool.determineDepartments()
    // tool.readCommuneIds()
    // tool.communes()
    tool.boundaryTagValues()
  }
}

class LocationExplorerTool(overpassQueryExecutor: OverpassQueryExecutor) {

  private val dir = "/kpn/conf/locations/fr-cc"
  private val geomFactory = new GeometryFactory

  def boundaryTagValues(): Unit = {
    val locationJsons = new LocationDefinitionReader("", Country.fr).franceCommuneLocationJsons()
    val values = locationJsons.flatMap(_.properties.all_tags.get("boundary"))
    val valueCounts = values.groupBy(identity).map(e => e._1 -> e._2.size)
    valueCounts.foreach { case(value, count) =>
      println(s"$value -> $count")
    }
  }

  def communes(): Unit = {
    println("Read France locations")
    val france = new LocationConfigurationReader().read().locations.filter(_.name == "fr").head
    val departements = france.children
    println(s"${departements.size} departements")

    println("Read France communes")
    val communeLocations = new LocationDefinitionReader("", Country.fr).franceCommunes()
    println(s"${communeLocations.size} communes")

    val cdcs = {
      val locationIds = getLocationIds()
      locationIds.flatMap { locationId =>
        val rawData = OsmDataXmlReader.read(s"$dir/$locationId.xml")
        val data = new DataBuilder(rawData).data
        val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", "CC"))
        if (locationRelations.size != 1) {
          println(s"unexpected number of location relations in $locationId.xml: ${locationRelations.size}")
          None
        }
        else {
          val relation = locationRelations.head
          val geometry = new GeometryCollection(xxxx(data, relation).toArray, geomFactory)
          val name = relation.tags("name").getOrElse("no-name")
          Some(
            LocationDefinition(
              id = name,
              name = name,
              level = 0,
              locationNames = Map.empty,
              boundingBox = geometry.getEnvelopeInternal,
              geometry = geometry,
              children = Seq.empty
            )
          )
        }
      }
    }

    val count = new AtomicInteger(0)
    val results = communeLocations.par.flatMap { commune =>
      val index = count.incrementAndGet()
      println(s"$index/${communeLocations.size}")
      if (departements.exists(departement => overlap(departement.geometry, commune.geometry) > 0.95)) {
        if (!cdcs.exists(cdc => overlap(cdc.geometry, commune.geometry) > 0.95)) {
          println(s"""RESULT "${commune.id}",""")
          Some(commune.id)
        }
        else {
          None
        }
      }
      else {
        None
      }
    }

    println("Done")
    results.foreach(println)
  }

  def determineDepartments(): Unit = {
    val france = new LocationConfigurationReader().read().locations.filter(_.name == "fr").head
    val franceGeometry = locationGeometry(france)
    val locationIds = getLocationIds()
    locationIds.foreach { locationId =>
      val rawData = OsmDataXmlReader.read(s"$dir/$locationId.xml")
      val data = new DataBuilder(rawData).data
      val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", "CC"))
      if (locationRelations.size != 1) {
        println(s"unexpected number of location relations in $locationId.xml: ${locationRelations.size}")
        Seq.empty
      }
      else {
        val relation = locationRelations.head
        val geometry = new GeometryCollection(xxxx(data, relation).toArray, geomFactory)

        val departements = france.children.filter { departement =>
          overlap(departement.geometry, geometry) > 0.05
        }

        if (departements.isEmpty) {
          println("    NO DEPARTEMENT")
        }
        else if (departements.size > 1) {
          println("    DEPARTEMENTS")
          departements.foreach { departement =>
            val percent = (overlap(departement.geometry, geometry) * 100).toInt
            println(s"   $percent%  ${departement.name}")
          }
        }
      }
    }
  }

  def drawExistingConfigurationImages(): Unit = {
    val locationConfiguration = new LocationConfigurationReader().read()
    locationConfiguration.locations.foreach { location =>
      val geometry = locationGeometry(location)
      val envelope = geometry.getEnvelopeInternal
      val imageWriter = new GeometryImageWriter(1000, 1000, envelope)

      location.children.foreach { locationLevel1 =>
        imageWriter.fill(locationLevel1.geometry, Color.yellow)
        locationLevel1.children.foreach { locationLevel2 =>
          imageWriter.fill(locationLevel2.geometry, Color.green)
          locationLevel2.children.foreach { locationLevel3 =>
            imageWriter.fill(locationLevel3.geometry, Color.red)
            locationLevel3.children.foreach { locationLevel4 =>
              imageWriter.fill(locationLevel4.geometry, Color.orange)
            }
          }
        }
      }

      location.children.foreach { locationLevel1 =>
        locationLevel1.children.foreach { locationLevel2 =>
          locationLevel2.children.foreach { locationLevel3 =>
            locationLevel3.children.foreach { locationLevel4 =>
              imageWriter.draw(locationLevel4.geometry, Color.gray, 1)
            }
            imageWriter.draw(locationLevel3.geometry, Color.black, 1)
          }
          imageWriter.draw(locationLevel2.geometry, Color.white, 2)
        }
        imageWriter.draw(locationLevel1.geometry, Color.blue, 2)
      }

      imageWriter.draw(location.geometry, Color.blue, 3)
      imageWriter.write(s"/kpn/location-images/${location.name}.png")
    }
  }

  def exploreCC(): Unit = {
    val france = new LocationConfigurationReader().read().locations.filter(_.name == "fr").head
    val franceGeometry = locationGeometry(france)
    val envelope = franceGeometry.getEnvelopeInternal
    val imageWriter = new GeometryImageWriter(1000, 1000, envelope)

    france.children.foreach { department =>
      imageWriter.draw(department.geometry, Color.red, 2)
    }

    imageWriter.draw(franceGeometry, Color.blue, 3)

    val ccsInMultipleDepartements = Seq(
      1647710,
      1663513,
      1992967,
      2155090,
      3031637,
      3402933,
      3406146,
      3438430,
      3438840,
      6852116,
      6883410,
      6952860,
      6983394
    )

    val locationIds = getLocationIds()
    locationIds.foreach { locationId =>
      val rawData = OsmDataXmlReader.read(s"$dir/$locationId.xml")
      val data = new DataBuilder(rawData).data
      val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", "CC"))
      if (locationRelations.size != 1) {
        println(s"unexpected number of location relations in $locationId.xml: ${locationRelations.size}")
        Seq.empty
      }
      else {
        val relation = locationRelations.head
        val geometry = new GeometryCollection(xxxx(data, relation).toArray, geomFactory)
        val color = if (ccsInMultipleDepartements.contains(relation.id)) {
          new Color(0, 230, 0, 128)
        }
        else {
          new Color(230, 230, 230, 128)
        }
        imageWriter.fill(geometry, color)
        imageWriter.draw(geometry, new Color(0, 0, 0, 128), 1)
      }
    }

    val communeLocations = new LocationDefinitionReader("", Country.fr).franceCommunes()
    val communeColor = new Color(230, 230, 0, 128)
    communeLocations.foreach { communeLocation =>
      if (NonCdcCommunes.get.contains(communeLocation.name)) {
        imageWriter.fill(communeLocation.geometry, communeColor)
        imageWriter.draw(communeLocation.geometry, new Color(0, 0, 0, 128), 1)
      }
    }

    imageWriter.write(s"/kpn/location-images/fr-cc-with-communes.png")
  }

  def writeFrance(): Unit = {
    val query = QueryRelation(1403916)
    val xmlString = overpassQueryExecutor.executeQuery(None, query)
    val file = new File(s"/kpn/conf/locations/fr-cc/france.xml")
    FileUtils.writeStringToFile(file, xmlString, "UTF-8")
  }

  def writeLocationFiles(): Unit = {
    readLocationIds().foreach { locationId =>
      println(locationId)
      val query = QueryRelation(locationId)
      val xmlString = overpassQueryExecutor.executeQuery(None, query)
      val file = new File(s"/kpn/conf/locations/fr-cc/$locationId.xml")
      FileUtils.writeStringToFile(file, xmlString, "UTF-8")
    }
  }

  def downloadCommune(): Unit = {
    readLocationIds().foreach { locationId =>
      println(locationId)
      val query = QueryRelation(locationId)
      val xmlString = overpassQueryExecutor.executeQuery(None, query)
      val file = new File(s"/kpn/conf/locations/fr-cc/$locationId.xml")
      FileUtils.writeStringToFile(file, xmlString, "UTF-8")
    }
  }

  private def readCommuneIds(): Seq[Long] = {
    val query = "relation['local_authority:FR'='CC'];out ids;"
    val xmlString = overpassQueryExecutor.execute(query)
    val file = new File(s"/kpn/conf/locations/fr-cc/ids.xml")
    FileUtils.writeStringToFile(file, xmlString, "UTF-8")
    val xml = XML.loadString(xmlString)
    (xml \ "relation").map { n => (n \ "@id").text.toLong }.distinct.sorted
  }


  def readLocationIds(): Seq[Long] = {
    val query = "relation['local_authority:FR'='CC'];out ids;"
    val xmlString = overpassQueryExecutor.execute(query)
    val file = new File(s"/kpn/conf/locations/fr-cc/ids.xml")
    FileUtils.writeStringToFile(file, xmlString, "UTF-8")
    val xml = XML.loadString(xmlString)
    (xml \ "relation").map { n => (n \ "@id").text.toLong }.distinct.sorted
  }

  private def countryBorder(): Geometry = {
    val filename = "/kpn/conf/locations/fr/Metropolitan France_1403916_AL3.GeoJson"
    LocationDefinitionReader.read(filename)
  }

  private def locationGeometry(locationDefinition: LocationDefinition): Geometry = {
    if (locationDefinition.name == "nl" || locationDefinition.name == "es") {
      val allChildren = locationDefinition.allChilderen().filterNot(location => LocationConfigurationDefinition.excludedLocations.contains(location.id))
      val bboxes = allChildren.map(_.geometry.getEnvelopeInternal)
      val xx = bboxes.tail.foldLeft(bboxes.head.copy) {
        (d1, d2) =>
          d1.expandToInclude(d2)
          d1
      }
      val clipper = new GeometryClipper(xx)
      clipper.clipSafe(locationDefinition.geometry, false, 0.00001)
    }
    else {
      locationDefinition.geometry
    }
  }

  private def overlap(parentGeometry: Geometry, childGeometry: Geometry): Double = {
    val intersection = parentGeometry.intersection(childGeometry)
    val intersectionArea = intersection.getArea
    val childLocationArea = childGeometry.getArea
    Math.abs(intersectionArea / childLocationArea)
  }

  private def xxxx(data: Data, relation: Relation): Seq[Polygon] = {

    val name = relation.tags("name").getOrElse("no-name")
    val roles = relation.members.flatMap(_.role).distinct.sorted
    println(s"""${relation.id} "$name" ${roles.mkString(", ")}""")
    Seq("name:en", "name:nl", "name:fr", "name:de").foreach { tag =>
      relation.tags(tag) match {
        case None =>
        case Some(localName) => println(s"""  $tag="$localName"""")
      }
    }

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

  private def getLocationIds(): Seq[Long] = {
    val xmlString = FileUtils.readFileToString(new File(s"$dir/ids.xml"), "UTF-8")
    val xml = XML.loadString(xmlString)
    (xml \ "relation").map { n => (n \ "@id").text.toLong }.distinct.sorted
  }
}
