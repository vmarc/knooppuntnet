package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Language
import kpn.api.common.LocationInfo
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.api.custom.LocationKey
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class LocationServiceImpl(locationConfiguration: LocationConfiguration) extends LocationService {
  private val locationMap = {
    val countries = locationConfiguration.locations
    val all = countries ++ countries.flatMap(_.allChilderen())
    all.map(l => l.id -> l).toMap
  }

  override def locationDefinition(locationId: String): Option[LocationDefinition] = {
    locationMap.get(locationId)
  }

  override def name(language: Language, locationId: String): String = {
    locationMap.get(locationId) match {
      case Some(locationDefinition) => locationDefinition.name(language)
      case None => locationId
    }
  }

  override def replaceNames(language: Language, routeLocationAnalysis: RouteLocationAnalysis): RouteLocationAnalysis = {
    val location = routeLocationAnalysis.location.map(location => locationReplace(language, location))
    val candidates = routeLocationAnalysis.candidates.map { candidate =>
      LocationCandidate(locationReplace(language, candidate.location), candidate.percentage)
    }
    val locationNames = routeLocationAnalysis.locationNames.map(n => name(language, n))
    RouteLocationAnalysis(
      location,
      candidates,
      locationNames
    )
  }

  def locationReplace(language: Language, location: Location): Location = {
    Location(
      location.names.map(n => name(language, n))
    )
  }

  override def toSubset(language: Language, locationKey: LocationKey): LocationSubset = {
    if (locationKey.name == ParcDuVercors.name) {
      LocationSubset(
        locationKey.name,
        locationKey.networkType,
        ParcDuVercors.communes
      )
    }
    else {
      LocationSubset(
        locationKey.name,
        locationKey.networkType,
        Seq(toId(language, locationKey.name))
      )
    }
  }

  override def toId(language: Language, location: String): String = {
    val nameParts = location.split(":").toSeq
    lookup(language, locationConfiguration.locations, nameParts, None) match {
      case Some(locationDefinition) => locationDefinition.id
      case None => location
    }
  }

  override def toInfos(language: Language, all: Seq[String], locations: Seq[String]): Seq[LocationInfo] = {
    val country = all.head.toLowerCase
    locations.map { location =>
      val locationName = name(language, location)
      val index = all.indexOf(location)
      val translatedLocations = all.take(index + 1).map(n => name(language, n))
      val mergedLocations = translatedLocations.mkString(":")
      val link = s"$country/$mergedLocations"
      LocationInfo(locationName, link)
    }
  }

  @tailrec
  private def lookup(
    language: Language,
    locations: Seq[LocationDefinition],
    locationNames: Seq[String],
    foundLocationDefinition: Option[LocationDefinition]
  ): Option[LocationDefinition] = {
    if (locationNames.isEmpty) {
      foundLocationDefinition
    }
    else {
      val localLocationName = name(language, locationNames.head)
      findLocation(language, locations, localLocationName) match {
        case None => None
        case Some(locationDefinition) =>
          lookup(
            language,
            locationDefinition.children,
            locationNames.tail,
            Some(locationDefinition)
          )
      }
    }
  }

  private def findLocation(
    language: Language,
    locations: Seq[LocationDefinition],
    localLocationName: String
  ): Option[LocationDefinition] = {
    locations.find(_.name(language) == localLocationName) match {
      case Some(locationDefinition) => Some(locationDefinition)
      case None =>
        val definitions = Language.values.filterNot(_ == language).flatMap { otherLanguage =>
          locations.find(_.name(otherLanguage) == localLocationName)
        }
        definitions.headOption
    }
  }
}
