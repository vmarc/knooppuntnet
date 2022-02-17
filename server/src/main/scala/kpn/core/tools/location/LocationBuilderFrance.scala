package kpn.core.tools.location

import kpn.api.common.DE
import kpn.api.common.NL
import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath
import kpn.core.util.Log
import org.apache.commons.io.FileUtils

import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.xml.XML

case class FranceContext(
  country: LocationData,
  departments: Seq[LocationData] = Seq.empty,
  intermunicipalities: Seq[LocationData] = Seq.empty,
  municipalities: Seq[LocationData] = Seq.empty
)

class LocationBuilderFrance(dir: String) {

  private val regionsFilename = s"$dir/fr-level-3.geojson.gz"
  private val departmentsFilename = s"$dir/fr-level-6.geojson.gz"
  private val municipalitiesFilename = s"$dir/fr-level-8.geojson.gz"
  private val intermunicipalitiesDir = s"$dir/../fr-intermunicipalities"

  private val log = Log(classOf[LocationBuilderFrance])

  def build(): Seq[LocationData] = {
    Log.context("fr") {
      val context1 = buildCountry()
      val context2 = buildDepartments(context1)
      val context3 = buildIntermunicipalities(context2)
      val context4 = buildMunicipalities(context3)
      Seq(context4.country) ++ context4.departments ++ context4.intermunicipalities ++ context4.municipalities
    }
  }

  private def buildCountry(): FranceContext = {
    Log.context("country") {
      val regions = InterpretedLocationJson.load(regionsFilename)
      val countryLocationJson = regions.filter(_.locationJson.properties.name == "Metropolitan France").head
      val country = LocationData(
        "fr",
        "France",
        Seq(
          LocationName(NL, "Frankrijk"),
          LocationName(DE, "Frankreich")
        ),
        LocationGeometry(countryLocationJson.geometry)
      )
      FranceContext(country)
    }
  }

  private def buildDepartments(context: FranceContext): FranceContext = {
    Log.context("departments") {
      val departmentLocationJsons = loadDepartments(context.country)
      val departments = departmentLocationJsons.zipWithIndex.map { case (department, index) =>
        val id = s"fr-1-${department.tags("ref:INSEE")}"
        val name = department.tags("name")
        log.info(s"${index + 1}/${departmentLocationJsons.size} $id $name")
        val names = department.names
        LocationData.from(
          id,
          Seq("fr"),
          name,
          names,
          LocationGeometry(department.geometry)
        )
      }
      context.copy(departments = departments)
    }
  }

  private def buildIntermunicipalities(context: FranceContext): FranceContext = {
    Log.context("intermunicipalities") {
      val intermunicipalities = FranceIntermunicipalities.types.flatMap { intermunicipalityType =>
        val locationIds = franceIntermunicipalityIds(intermunicipalityType)
        val count = new AtomicInteger(0)
        val logContext = Log.contextMessages
        locationIds.par.flatMap { locationId =>
          val index = count.incrementAndGet()
          Log.context(logContext ++ Seq(s"$index/${locationIds.size}", locationId.toString)) {
            val locationData = new FranceIntermunicipalityReader(intermunicipalitiesDir, locationId, intermunicipalityType).read()
            log.info(locationData.name)
            if (context.country.contains(locationData.geometry)) {
              val parentDepartments = context.departments.filter { department =>
                department.geometry.overlap(locationData.geometry) > 0.05
              }
              if (parentDepartments.isEmpty) {
                throw new RuntimeException("department not found")
              }
              val parents = parentDepartments.map(department => LocationPath(Seq("fr", department.id)))
              Some(
                locationData.copy(
                  paths = parents
                )
              )
            }
            else {
              None
            }
          }
        }
      }
      context.copy(intermunicipalities = intermunicipalities)
    }
  }

  private def buildMunicipalities(context: FranceContext): FranceContext = {
    Log.context("municipalities") {
      val municipalityLocationJsons = loadMunicipalities(context.country)
      val count = new AtomicInteger(0)
      val logContext = Log.contextMessages
      val municipalities = municipalityLocationJsons.par.flatMap { municipality =>
        val index = count.incrementAndGet()
        val id = s"fr-3-${municipality.tags("ref:INSEE")}"
        Log.context(logContext ++ Seq(s"$index/${municipalityLocationJsons.size}", id)) {
          val name = municipality.tags("name")
          log.info(name)
          if (context.country.contains(LocationGeometry(municipality.geometry))) {
            val names = municipality.names
            val municipalityGeometry = LocationGeometry(municipality.geometry)
            context.departments.find(_.contains(municipalityGeometry)) match {
              case None => throw new RuntimeException(s"No parent found for municipality $id $name")
              case Some(department) =>
                val intermunicipalityOption = FranceIntermunicipalities.intermunicipalities.find(_.municipalityIds.contains(municipality.relationId)) match {
                  case Some(intermunicipality) =>
                    context.intermunicipalities.find(_.id == intermunicipality.intermunicipalityId)
                  case None =>
                    // TODO volgende zou veel sneller gaan als er alleen naar de intermunicipalities binnen het departement gekeken wordt?
                    context.intermunicipalities.find(_.contains(municipalityGeometry))
                }
                val parents = intermunicipalityOption match {
                  case Some(intermunicipality) => Seq("fr", department.id, intermunicipality.id)
                  case None =>
                    log.warn(s"Municipality zonder intermunicipality: $id ${municipality.relationId} ${municipality.name}")
                    Seq("fr", department.id)
                }
                Some(
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
          else {
            None
          }
        }
      }
      context.copy(municipalities = municipalities.seq.toSeq)
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

  private def loadMunicipalities(country: LocationData): Seq[InterpretedLocationJson] = {
    log.info("Read municipalities file")
    InterpretedLocationJson.load(municipalitiesFilename)
      .filter(_.tags.contains("ref:INSEE"))
      .sortBy(_.tags("ref:INSEE"))
  }


  private def franceIntermunicipalityIds(intermunicipalityType: String): Seq[Long] = {
    val filename = s"$intermunicipalitiesDir/fr-${
      intermunicipalityType.toLowerCase
    }.xml"
    val xmlString = FileUtils.readFileToString(new File(filename), "UTF-8")
    val xml = XML.loadString(xmlString)
    (xml \ "relation").map {
      n => (n \ "@id").text.toLong
    }.distinct.sorted
  }
}
