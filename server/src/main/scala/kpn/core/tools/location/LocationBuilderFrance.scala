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
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.xml.XML

class LocationBuilderFrance(dir: String) {

  private val regionsFilename = s"$dir/fr-level-3.geojson.gz"
  private val departmentsFilename = s"$dir/fr-level-6.geojson.gz"
  private val communesFilename = s"$dir/fr-level-8.geojson.gz"
  private val locationDatas = new LocationDatas()

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
      val regions = InterpretedLocationJson.load(regionsFilename)
      val country = regions.filter(_.locationJson.properties.name == "Metropolitan France").head
      locationDatas.add(
        LocationData(
          "fr",
          "France",
          Seq(
            LocationName(NL, "Frankrijk"),
            LocationName(DE, "Frankreich")
          ),
          LocationGeometry(country.geometry)
        )
      )
    }
  }

  private def buildDepartments(): Unit = {
    Log.context("departments") {
      locationDatas.toSeq.find(_.id == "fr") match {
        case None => throw new RuntimeException("Could not find 'fr' location")
        case Some(country) =>
          val departments = loadDepartments(country)
          departments.zipWithIndex.foreach { case (department, index) =>
            val id = {
              val inseeCode = department.tags("ref:INSEE")
              s"fr-1-$inseeCode"
            }
            val name = department.tags("name")
            log.info(s"${index + 1}/${departments.size} $id $name")
            val names = department.names
            locationDatas.add(
              LocationData.from(
                id,
                Seq("fr"),
                name,
                names,
                LocationGeometry(department.geometry)
              )
            )
          }
      }
    }
  }

  private def buildCdcs(): Unit = {
    Log.context("cdc") {
      locationDatas.toSeq.find(_.id == "fr") match {
        case None => throw new RuntimeException("Could not find 'fr' location")
        case Some(country) =>
          val departments = locationDatas.startingWith("fr-1")
          val geomFactory = new GeometryFactory
          val locationIds = franceCdcLocationIds()
          val count = new AtomicInteger(0)
          val context = Log.contextMessages
          locationIds.par.foreach { locationId =>
            Log.context(context :+ locationId.toString) {
              val index = count.incrementAndGet()
              val rawData = OsmDataXmlReader.read(s"/kpn/locations/fr-cdc/$locationId.xml")
              val data = new DataBuilder(rawData).data
              val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", "CC"))
              if (locationRelations.size != 1) {
                throw new RuntimeException(s"Unexpected number of location relations in $locationId.xml: ${locationRelations.size}")
              }
              val relation = locationRelations.head
              relation.tags("name") match {
                case None => log.warn(s"$index/${locationIds.size} name not found")
                case Some(name) =>
                  log.info(s"$index/${locationIds.size} $name")
                  relation.tags("ref:FR:SIREN") match {
                    case None => log.warn(s"SIREN code not found")
                    case Some(sirenCode) =>
                      val id = s"fr-2-$sirenCode"
                      val polygons = toPolygons(data, relation)
                      val geometry = if (polygons.size != 1) {
                        polygons.head
                      }
                      else {
                        new GeometryCollection(polygons.toArray, geomFactory)
                      }
                      val cdcGeometry = LocationGeometry(geometry)

                      if (country.contains(cdcGeometry)) {
                        val parentDepartments = departments.filter { department =>
                          department.geometry.overlap(cdcGeometry) > 0.05
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
                          locationDatas.add(
                            LocationData(
                              id,
                              parents,
                              name,
                              names,
                              LocationGeometry(cdcGeometry.geometry)
                            )
                          )
                        }
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
      locationDatas.toSeq.find(_.id == "fr") match {
        case None => throw new RuntimeException("Could not find 'fr' location")
        case Some(country) =>
          val departments = locationDatas.startingWith("fr-1")
          val cdcs = locationDatas.startingWith("fr-2")
          val communes = InterpretedLocationJson.load(communesFilename)
            .filter(_.tags.contains("ref:INSEE"))
            .sortBy(_.tags("ref:INSEE"))
          val count = new AtomicInteger(0)
          val context = Log.contextMessages
          communes.par.foreach { commune =>
            val index = count.incrementAndGet()
            Log.context(context :+ s"$index/${communes.size}") {
              val id = {
                val inseeCode = commune.tags("ref:INSEE")
                s"fr-3-$inseeCode"
              }
              val name = commune.tags("name")
              log.info(s"$id $name")
              val names = commune.names
              val communeGeometry = LocationGeometry(commune.geometry)
              if (country.contains(communeGeometry)) {
                departments.find(_.contains(communeGeometry)) match {
                  case None => throw new RuntimeException(s"No parent found for commune $id $name")
                  case Some(department) =>
                    val parents = cdcs.find(_.contains(communeGeometry)) match {
                      case Some(cdc) => Seq("fr", department.id, cdc.id)
                      case None => Seq("fr", department.id)
                    }
                    locationDatas.add(
                      LocationData.from(
                        id,
                        parents,
                        name,
                        names,
                        LocationGeometry(commune.geometry)
                      )
                    )
                }
              }
            }
          }
      }
    }
  }

  private def loadDepartments(country: LocationData): Seq[InterpretedLocationJson] = {
    log.info("Read departments file")
    val departments = InterpretedLocationJson.load(departmentsFilename)
    log.info("Filtering departments")
    departments
      .filter(_.tags.contains("ref:INSEE"))
      .filter(department => country.contains(LocationGeometry(department.geometry)))
      .sortBy(_.tags("ref:INSEE"))
  }

  private def franceCdcLocationIds(): Seq[Long] = {
    val xmlString = FileUtils.readFileToString(new File(s"/kpn/locations/fr-cdc/ids.xml"), "UTF-8")
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
}
