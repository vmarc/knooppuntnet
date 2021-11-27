package kpn.core.tools.location

import kpn.api.common.DE
import kpn.api.common.Languages
import kpn.api.common.NL
import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.tools.country.PolygonBuilder
import kpn.core.tools.country.SkeletonData
import kpn.core.tools.country.SkeletonNode
import kpn.core.tools.country.SkeletonRelation
import kpn.core.tools.country.SkeletonWay
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationJson
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationsJson
import kpn.server.json.Json
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.GZIPInputStream
import scala.collection.mutable.ListBuffer
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.xml.XML

class LocationBuilderFrance {

  private val dir = "/kpn/locations"
  private val regionsFilename = s"$dir/fr-regions.geojson.gz"
  private val departmentsFilename = s"$dir/fr-departments.geojson.gz"
  private val communesFilename = s"$dir/fr-communes.geojson.gz"
  private val locationDatas = ListBuffer[LocationData]()

  private val log = Log(classOf[LocationBuilderFrance])

  def build(): Seq[LocationData] = {
    Log.context("fr") {
      buildCountry()
      buildDepartments()
      buildCdcs()
      loadCommunes()
      locationDatas.toSeq
    }
  }

  private def buildCountry(): Unit = {
    Log.context("country") {
      val locationJsons = loadLocationJsons(regionsFilename)
      val locationJson = locationJsons.filter(_.properties.name == "Metropolitan France").head
      val names = Seq(
        LocationName(NL, "Frankrijk"),
        LocationName(DE, "Frankreich")
      )
      val data = LocationData(
        "fr",
        LocationDoc("fr", Seq.empty, "France", names),
        LocationGeometry(locationJson.geometry)
      )
      addLocation(data)
    }
  }

  private def buildDepartments(): Unit = {
    Log.context("departments") {
      locationDatas.find(_.id == "fr") match {
        case None => log.warn("Could not find 'fr' location")
        case Some(country) =>
          val departmentJsons = loadDepartmentJsons(country)
          val count = new AtomicInteger(0)
          departmentJsons.foreach { departmentJson =>
            val index = count.incrementAndGet()
            val id = {
              val inseeCode = departmentJson.properties.all_tags("ref:INSEE")
              s"fr-1-$inseeCode"
            }
            val name = departmentJson.properties.all_tags("name")
            log.info(s"$index/${departmentJsons.size} $id $name")
            val names = locationNames(departmentJson)
            val data = LocationData(
              id,
              LocationDoc(id, Seq(LocationPath(Seq("fr"))), name, names),
              LocationGeometry(departmentJson.geometry)
            )
            addLocation(data)
          }
      }
    }
  }

  private def buildCdcs(): Unit = {
    Log.context("cdc") {
      val departments = locationDatas.toSeq.filter(_.id.startsWith("fr-1"))
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
                relation.tags("ref:FR:SIREN") match {
                  case None => log.warn(s"SIREN code not found")
                  case Some(sirenCode) =>
                    val id = s"fr-2-$sirenCode"
                    val geometry = LocationGeometry(new GeometryCollection(xxxx(data, relation).toArray, geomFactory))
                    val parentDepartments = departments.filter { department =>
                      department.geometry.overlap(geometry) > 0.05
                    }
                    if (parentDepartments.isEmpty) {
                      log.warn(s"No department found")
                    }
                    else {
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

                      val parents = parentDepartments.map(department => LocationPath(Seq("fr", department.id)))
                      val data = LocationData(
                        id,
                        LocationDoc(id, parents, name, names),
                        LocationGeometry(geometry.geometry)
                      )
                      addLocation(data)
                    }
                }
            }
          }
        }
      }
    }
  }

  private def loadCommunes(): Unit = {
    Log.context("communes") {
      val departments = locationDatas.toSeq.filter(_.id.startsWith("fr-1"))
      val cdcs = locationDatas.toSeq.filter(_.id.startsWith("fr-2"))
      val communeJsons = loadCommuneJsons()
      val count = new AtomicInteger(0)
      communeJsons.par.foreach { commune =>
        val index = count.incrementAndGet()
        Log.context(s"$index/${communeJsons.size}") {
          val id = {
            val inseeCode = commune.properties.all_tags("ref:INSEE")
            s"fr-3-$inseeCode"
          }
          val name = commune.properties.all_tags("name")
          log.info(s"$id $name")
          val names = locationNames(commune)
          val communeGeometry = LocationGeometry(commune.geometry)
          departments.find(department => department.geometry.contains(communeGeometry)) match {
            case None => log.error("No parent found for commune")
            case Some(department) =>
              val parents = cdcs.find(cdc => cdc.geometry.contains(communeGeometry)) match {
                case Some(cdc) => Seq("fr", department.id, cdc.id)
                case None => Seq("fr", department.id)
              }
              val data = LocationData(
                id,
                LocationDoc(id, Seq(LocationPath(parents)), name, names),
                LocationGeometry(commune.geometry)
              )
              addLocation(data)
          }
        }
      }
    }
  }

  private def loadDepartmentJsons(country: LocationData): Seq[LocationJson] = {
    log.info("Read departments file")
    val locationJsons = loadLocationJsons(departmentsFilename)
    log.info("Filtering departments")
    locationJsons
      .filter(_.properties.all_tags.get("boundary").contains("administrative"))
      .filter(_.properties.all_tags.contains("ref:INSEE"))
      .filter(department => country.geometry.contains(LocationGeometry(department.geometry)))
      .sortBy(_.properties.all_tags("ref:INSEE"))
  }

  private def loadCommuneJsons(): Seq[LocationJson] = {
    log.infoElapsed {
      log.info("Read communes file")
      val locationJsons = loadLocationJsons(communesFilename)
        .filter(_.properties.all_tags.get("boundary").contains("administrative"))
        .filter(_.properties.all_tags.contains("ref:INSEE"))
        .sortBy(_.properties.all_tags("ref:INSEE"))
      (s"${locationJsons.size} communes loaded", locationJsons)
    }
  }

  private def loadLocationJsons(filename: String): Seq[LocationJson] = {
    val gzippedInputStream = new FileInputStream(filename)
    val ungzippedInputStream = new GZIPInputStream(gzippedInputStream)
    val fileReader = new InputStreamReader(ungzippedInputStream, "UTF-8")
    Json.objectMapper.readValue(fileReader, classOf[LocationsJson]).features
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

  private def addLocation(data: LocationData): Unit = {
    synchronized {
      locationDatas += data
    }
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
