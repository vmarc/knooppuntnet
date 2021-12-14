package kpn.core.tools.location

import kpn.core.doc.LocationPath
import kpn.core.util.Log

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.ListBuffer
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

class LocationBuilderGermany(dir: String) {

  private val countryFilename = s"$dir/de-level-2.geojson.gz"
  private val federalStatesFilename = s"$dir/de-level-4.geojson.gz"
  private val districtFilename = s"$dir/de-level-5.geojson.gz"
  private val countyFilename = s"$dir/de-level-6.geojson.gz"
  private val locationDatas = ListBuffer[LocationData]()

  private val log = Log(classOf[LocationBuilderGermany])

  def build(): Seq[LocationData] = {
    Log.context("de") {
      buildCountry()
      buildFederalStates()
      loadDistricts()
      loadCounties()
      locationDatas.toSeq
    }
  }

  private def buildCountry(): Unit = {
    Log.context("country") {
      val locationJson = InterpretedLocationJson.load(countryFilename).head
      val data = LocationData(
        "de",
        LocationDoc("de", Seq.empty, locationJson.name, locationJson.names),
        LocationGeometry(locationJson.geometry)
      )
      addLocation(data)
    }
  }

  private def buildFederalStates(): Unit = {
    Log.context("federal states") {
      val federalStates = InterpretedLocationJson.load(federalStatesFilename)
      federalStates.zipWithIndex.foreach { case (federalState, index) =>
        federalState.tags.get("de:regionalschluessel") match {
          case Some(tagValue) =>
            val id = s"de-1-$tagValue"
            log.info(s"${index + 1}/${federalStates.size} $id ${federalState.name}")
            val data = LocationData(
              id,
              LocationDoc(id, Seq(LocationPath(Seq("de"))), federalState.name, federalState.names),
              LocationGeometry(federalState.geometry)
            )
            addLocation(data)
          case None =>
            log.error(s"federal state, no id found for ${federalState.name}")
        }
      }
    }
  }

  private def loadDistricts(): Unit = {
    Log.context("districts") {
      val federalStates = locationDatas.toSeq.filter(_.id.startsWith("de-1"))
      val districts = InterpretedLocationJson.load(districtFilename)
      val count = new AtomicInteger(0)
      val context = Log.contextMessages
      districts.par.foreach { district =>
        val index = count.incrementAndGet()
        Log.context(context :+ s"$index/${districts.size}") {
          val id = s"de-2-${district.tags("de:regionalschluessel")}"
          log.info(s"$id ${district.name}")
          val districtGeometry = LocationGeometry(district.geometry)
          federalStates.find(federalState => federalState.geometry.contains(districtGeometry)) match {
            case None => log.error(s"No parent found for municipality $id ${district.name}")
            case Some(federalState) =>
              val parents = Seq("de", federalState.id)
              val data = LocationData(
                id,
                LocationDoc(id, Seq(LocationPath(parents)), district.name, district.names),
                LocationGeometry(district.geometry)
              )
              addLocation(data)
          }
        }
      }
    }
  }

  private def loadCounties(): Unit = {
    Log.context("counties") {
      val federalStates = locationDatas.toSeq.filter(_.id.startsWith("de-1"))
      val districts = locationDatas.toSeq.filter(_.id.startsWith("de-2"))
      val counties = InterpretedLocationJson.load(countyFilename)
      val count = new AtomicInteger(0)
      val context = Log.contextMessages
      counties.par.foreach { county =>
        val index = count.incrementAndGet()
        Log.context(context :+ s"$index/${counties.size}") {
          if (county.tags.get("boundary").contains("administrative")) {
            val refOption = county.tags.get("de:amtlicher_gemeindeschluessel") match {
              case Some(value) => Some(value)
              case None =>
                county.tags.get("de:regionalschluessel") match {
                  case Some(value) =>
                    Some(value)
                  case None =>
                    log.error(s"Reference not found in county ${county.name}")
                    None
                }
            }

            refOption match {
              case None =>
              case Some(ref) =>
                val id = s"de-3-$ref"
                log.info(s"$id ${county.name}")
                val countyGeometry = LocationGeometry(county.geometry)
                districts.find(district => district.geometry.contains(countyGeometry)) match {
                  case Some(district) =>
                    if (district.doc.paths.size == 1) {
                      val parents = district.doc.paths.head.locationIds :+ district.id
                      val data = LocationData(
                        id,
                        LocationDoc(id, Seq(LocationPath(parents)), county.name, county.names),
                        LocationGeometry(county.geometry)
                      )
                      addLocation(data)
                    }
                    else {
                      log.error(s"unexpected number of paths in district ${district.id} ${district.doc.name}")
                    }

                  case None =>
                    federalStates.find(federalState => federalState.geometry.contains(countyGeometry)) match {
                      case None =>
                        log.error(s"No parent found for county $id ${county.name}")
                      case Some(federalState) =>
                        val parents = federalState.doc.paths.head.locationIds :+ federalState.id
                        val data = LocationData(
                          id,
                          LocationDoc(id, Seq(LocationPath(parents)), county.name, county.names),
                          LocationGeometry(county.geometry)
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

  private def addLocation(data: LocationData): Unit = {
    synchronized {
      locationDatas += data
    }
  }
}
