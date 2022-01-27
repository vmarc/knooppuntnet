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
  private val municipalitiesFilename = s"$dir/fr-level-8.geojson.gz"
  private val intermunicipalitiesDir = s"$dir/../fr-intermunicipalities"
  private val locationDatas = new LocationDatas()

  private val log = Log(classOf[LocationBuilderFrance])

  def build(): Seq[LocationData] = {
    Log.context("fr") {
      buildCountry()
      buildDepartments()
      buildIntermunicipalities()
      // loadMunicipalities()
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

  private def buildIntermunicipalities(): Unit = {
    Log.context("intermunicipalities") {
      locationDatas.toSeq.find(_.id == "fr") match {
        case None => throw new RuntimeException("Could not find 'fr' location")
        case Some(country) =>
          val departments = locationDatas.startingWith("fr-1")
          val geomFactory = new GeometryFactory
          FranceIntermunicipalities.types.foreach { intermunicipalityType =>
            val locationIds = franceIntermunicipalityIds(intermunicipalityType)
            val count = new AtomicInteger(0)
            val context = Log.contextMessages
            locationIds.par.foreach { locationId =>
              Log.context(context :+ locationId.toString) {
                val index = count.incrementAndGet()
                val rawData = OsmDataXmlReader.read(s"$intermunicipalitiesDir/$locationId.xml")
                val data = new DataBuilder(rawData).data
                val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", intermunicipalityType))
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
  }

  private def loadMunicipalities(): Unit = {
    Log.context("municipalities") {
      locationDatas.toSeq.find(_.id == "fr") match {
        case None => throw new RuntimeException("Could not find 'fr' location")
        case Some(country) =>
          val departments = locationDatas.startingWith("fr-1")
          val intermunicipalities = locationDatas.startingWith("fr-2")
          val municipalities = InterpretedLocationJson.load(municipalitiesFilename)
            .filter(_.tags.contains("ref:INSEE"))
            .sortBy(_.tags("ref:INSEE"))
          val count = new AtomicInteger(0)
          val context = Log.contextMessages
          municipalities.par.foreach { municipality =>
            val index = count.incrementAndGet()
            if (!FranceIntermunicipalities.orphanMunicipalityIds.contains(municipality.relationId)) {
              Log.context(context :+ s"$index/${municipalities.size}") {
                val id = {
                  val inseeCode = municipality.tags("ref:INSEE")
                  s"fr-3-$inseeCode"
                }
                val name = municipality.tags("name")
                log.info(s"$id $name")
                val names = municipality.names
                val municipalityGeometry = LocationGeometry(municipality.geometry)
                if (country.contains(municipalityGeometry)) {
                  departments.find(_.contains(municipalityGeometry)) match {
                    case None => throw new RuntimeException(s"No parent found for municipality $id $name")
                    case Some(department) =>
                      val intermunicipalityOption = FranceIntermunicipalities.intermunicipalities.find(_.municipalityIds.contains(municipality.relationId)) match {
                        case Some(intermunicipality) =>
                          intermunicipalities.find(_.id == intermunicipality.intermunicipalityId)
                        case None =>
                          intermunicipalities.find(_.contains(municipalityGeometry))
                      }
                      val parents = intermunicipalityOption match {
                        case Some(intermunicipality) => Seq("fr", department.id, intermunicipality.id)
                        case None => Seq("fr", department.id)
                      }
                      locationDatas.add(
                        LocationData.from(
                          id,
                          parents,
                          name,
                          names,
                          LocationGeometry(municipality.geometry)
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

  private def loadDepartments(country: LocationData): Seq[InterpretedLocationJson] = {
    log.info("Read departments file")
    val departments = InterpretedLocationJson.load(departmentsFilename)
    log.info("Filtering departments")
    departments
      .filter(_.tags.contains("ref:INSEE"))
      .filter(department => country.contains(LocationGeometry(department.geometry)))
      .sortBy(_.tags("ref:INSEE"))
  }

  private def franceIntermunicipalityIds(intermunicipalityType: String): Seq[Long] = {
    val filename = s"$intermunicipalitiesDir/fr-${intermunicipalityType.toLowerCase}.xml"
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
}
