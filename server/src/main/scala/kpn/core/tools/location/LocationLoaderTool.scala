package kpn.core.tools.location

import kpn.api.common.DE
import kpn.api.common.Languages
import kpn.api.common.NL
import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.doc.LocationDoc
import kpn.core.doc.LocationGeometryDoc
import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.tools.country.PolygonBuilder
import kpn.core.tools.country.SkeletonData
import kpn.core.tools.country.SkeletonNode
import kpn.core.tools.country.SkeletonRelation
import kpn.core.tools.country.SkeletonWay
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationJson
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationsJson
import kpn.server.json.Json
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.gt
import org.mongodb.scala.model.Filters.lt

import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.GZIPInputStream
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.xml.XML

object LocationLoaderTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      new LocationLoaderTool(database).build()
    }
  }
}

class LocationLoaderTool(database: Database) {

  private val updateLocationGeometryDocs = false
  private val dir = "/kpn/locations"
  private val log = Log(classOf[LocationLoaderTool])

  def build(): Unit = {
    buildFrance()
  }

  private def buildFrance(): Unit = {
    Log.context("fr") {
      buildFranceCountry()
      buildFranceDepartments()
      buildFranceCdcLocations()
      loadFranceCommunes()
    }
  }

  private def buildFranceCountry(): Unit = {
    Log.context("country") {
      val locationJsons = loadLocationJsons("fr-regions.geojson.gz")
      val locationJson = locationJsons.filter(_.properties.name == "Metropolitan France").head
      val names = Seq(
        LocationName(NL, "Frankrijk"),
        LocationName(DE, "Frankreich")
      )
      val locationDoc = LocationDoc("fr", Seq.empty, Seq.empty, "France", names)
      database.locations.save(locationDoc)
      if (updateLocationGeometryDocs) {
        val locationGeometryDoc = LocationGeometryDoc("fr", locationJson.geometry)
        database.locationGeometries.save(locationGeometryDoc)
      }
    }
  }

  private def buildFranceDepartments(): Unit = {
    Log.context("departments") {

      log.info("Read departments file")
      val locationJsons = loadLocationJsons("fr-departments.geojson.gz")
      val countryGeometry = database.locationGeometries.findByStringId("fr").get.geometry

      val departments = locationJsons
        .filter(_.properties.all_tags.get("boundary").contains("administrative"))
        .filter(_.properties.all_tags.contains("ref:INSEE"))
        .filter(department => contains(countryGeometry, department.geometry))
        .sortBy(_.properties.all_tags("ref:INSEE"))

      val count = new AtomicInteger(0)
      departments.foreach { department =>
        val index = count.incrementAndGet()
        val inseeCode = department.properties.all_tags("ref:INSEE")
        val id = s"fr/1/$inseeCode"
        val name = department.properties.all_tags("name")
        log.info(s"$index/${departments.size} $id $name")
        val names = locationNames(department)
        val locationDoc = LocationDoc(id, Seq(LocationPath(Seq("fr"))), Seq("fr"), name, names)
        database.locations.save(locationDoc)
        if (updateLocationGeometryDocs) {
          val locationGeometryDoc = LocationGeometryDoc(id, department.geometry)
          database.locationGeometries.save(locationGeometryDoc)
        }
      }
    }
  }

  private def buildFranceCdcLocations(): Unit = {
    Log.context("cdc") {
      log.info(s"Loading department geometries from database")
      val departmentGeometries = database.locationGeometries.find[LocationGeometryDoc](
        and(
          gt("_id", "fr/1/"),
          lt("_id", "fr/2/")
        ),
        log
      )
      log.info(s"${departmentGeometries.size} department geometries loaded")

      val geomFactory = new GeometryFactory
      val locationIds = franceCdcLocationIds()

      val count = new AtomicInteger(0)
      locationIds.par.foreach { locationId =>
        Log.context(locationId.toString) {
          val index = count.incrementAndGet()
          val rawData = OsmDataXmlReader.read(s"$dir/fr-cdc/$locationId.xml")
          val data = new DataBuilder(rawData).data
          val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", "CC"))
          if (locationRelations.size != 1) {
            log.error(s"unexpected number of location relations in $locationId.xml: ${locationRelations.size}")
          }
          else {
            val relation = locationRelations.head
            relation.tags("name") match {
              case None => log.warn(s"$index/${locationIds.size} name not found")
              case Some(name) =>
                log.info(s"$index/${locationIds.size} $name")
                val geometry = new GeometryCollection(xxxx(data, relation).toArray, geomFactory)
                val departments = departmentGeometries.filter { departmentGeometry =>
                  overlap(departmentGeometry.geometry, geometry) > 0.05
                }
                if (departments.isEmpty) {
                  log.warn(s"No department found")
                }
                else {
                  relation.tags("ref:FR:SIREN") match {
                    case None => log.warn(s"SIREN code not found")
                    case Some(sirenCode) =>
                      val id = s"fr/2/$sirenCode"
                      val names = Languages.all.flatMap { language =>
                        val lang = language.toString.toLowerCase
                        relation.tags(s"name:$lang") match {
                          case None => None
                          case Some(value) =>
                            if (value != name) {
                              Some(
                                LocationName(
                                  language,
                                  value
                                )
                              )
                            }
                            else {
                              None
                            }
                        }
                      }

                      val parents = departments.map(_._id)
                      val locationDoc = LocationDoc(id, Seq(LocationPath(parents)), parents, name, names)
                      database.locations.save(locationDoc)
                      if (updateLocationGeometryDocs) {
                        val locationGeometryDoc = LocationGeometryDoc(id, geometry)
                        database.locationGeometries.save(locationGeometryDoc)
                      }
                  }
                }
            }
          }
        }
      }
    }
  }

  private def loadFranceCommunes(): Unit = {
    Log.context("communes") {

      log.info(s"Loading CDC geometries from database")
      val cdcGeometries = database.locationGeometries.find[LocationGeometryDoc](
        and(
          gt("_id", "fr/2/"),
          lt("_id", "fr/3/")
        ),
        log
      )
      log.info(s"${cdcGeometries.size} CDC geometries loaded")


      log.info("Read communes file")
      val locationJsons = loadLocationJsons("fr-communes.geojson.gz")
      log.info(s"${locationJsons.size} communes loaded")

      log.info("Load country geometry")
      val countryGeometry = database.locationGeometries.findByStringId("fr").get.geometry
      log.info("Country geometry loaded")

      val communes = locationJsons
        .filter(_.properties.all_tags.get("boundary").contains("administrative"))
        .filter(_.properties.all_tags.contains("ref:INSEE"))
        .sortBy(_.properties.all_tags("ref:INSEE"))

      val communesFrance = Log.context("filter") {
        val count = new AtomicInteger(0)
        communes.filter { commune =>
          val index = count.incrementAndGet()
          log.info(s"$index/${communes.size}")
          contains(countryGeometry, commune.geometry)
        }
      }

      Log.context("generate") {
        val count = new AtomicInteger(0)
        communesFrance.par.foreach { commune =>
          val index = count.incrementAndGet()
          Log.context(s"$index/${communes.size}") {
            val inseeCode = commune.properties.all_tags("ref:INSEE")
            val id = s"fr/3/$inseeCode"
            val name = commune.properties.all_tags("name")
            log.info(s"$id $name")
            val names = locationNames(commune)
            val parent = cdcGeometries.find(cdc => contains(cdc.geometry, commune.geometry)) match {
              case None => s"fr/1/${inseeCode.substring(0, 2)}"
              case Some(cdc) => cdc._id
            }
            val locationDoc = LocationDoc(id, Seq(LocationPath(Seq(parent))), Seq(parent), name, names)
            database.locations.save(locationDoc)
            if (updateLocationGeometryDocs) {
              val locationGeometryDoc = LocationGeometryDoc(id, commune.geometry)
              database.locationGeometries.save(locationGeometryDoc)
            }
          }
        }
      }
    }
  }

  private def loadLocationJsons(filename: String): Seq[LocationJson] = {
    val gzippedInputStream = new FileInputStream(s"$dir/$filename")
    val ungzippedInputStream = new GZIPInputStream(gzippedInputStream)
    val fileReader = new InputStreamReader(ungzippedInputStream, "UTF-8")
    Json.objectMapper.readValue(fileReader, classOf[LocationsJson]).features
  }

  private def contains(parentGeometry: Geometry, childGeometry: Geometry): Boolean = {
    overlap(parentGeometry, childGeometry) > 0.95
  }

  private def overlap(parentGeometry: Geometry, childGeometry: Geometry): Double = {
    val intersection = parentGeometry.intersection(childGeometry)
    val intersectionArea = intersection.getArea
    val childLocationArea = childGeometry.getArea
    Math.abs(intersectionArea / childLocationArea)
  }

  private def locationNames(locationJson: LocationJson): Seq[LocationName] = {
    val name = locationJson.properties.all_tags("name")
    Languages.all.flatMap { language =>
      val lang = language.toString.toLowerCase
      locationJson.properties.all_tags.get(s"name:$lang") match {
        case None => None
        case Some(value) =>
          if (value != name) {
            Some(
              LocationName(
                language,
                value
              )
            )
          }
          else {
            None
          }
      }
    }
  }

  private def franceCdcLocationIds(): Seq[Long] = {
    val xmlString = FileUtils.readFileToString(new File(s"$dir/fr-cdc/ids.xml"), "UTF-8")
    val xml = XML.loadString(xmlString)
    (xml \ "relation").map { n => (n \ "@id").text.toLong }.distinct.sorted
  }

  private def xxxx(data: Data, relation: Relation): Seq[Polygon] = {

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
}
