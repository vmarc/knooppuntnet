package kpn.core.load

import kpn.core.data.DataBuilder
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.load.data.LoadedRoute
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import kpn.core.overpass.QueryRelations
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.Timestamp

import scala.xml.XML

class RouteLoaderImpl(
  nonCachingExecutor: OverpassQueryExecutor,
  executor: OverpassQueryExecutor,
  countryAnalyzer: CountryAnalyzer
) extends RouteLoader {

  private val log = Log(classOf[RouteLoaderImpl])

  override def loadRoute(timestamp: Timestamp, routeId: Long): Option[LoadedRoute] = {

    val xmlString: String = log.elapsed {
      val xml = executor.executeQuery(Some(timestamp), QueryRelation(routeId))
      (s"Load route $routeId at ${timestamp.iso}", xml)
    }

    if (xmlString.isEmpty) {
      None
    }
    else {
      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: Exception =>
          throw new RuntimeException(s"Could not load route $routeId at ${timestamp.iso}\n---\n$xmlString\n---", e)
      }

      val rawData1 = new Parser().parse(xml.head)
      val rawData = rawData1.copy(timestamp = Some(timestamp))
      rawData.relationWithId(routeId) match {
        case None =>
          log.warn(s"Could not find route $routeId in raw data at ${timestamp.iso}, assume route does not exist, continue processing\n---\n$xmlString\n---")
          None

        case Some(rawRelation) =>
          rawRelation.tags("network").flatMap(NetworkType.withName) match {
            case Some(networkType) =>
              val data = new DataBuilder(networkType, rawData).data
              val relation = data.relations(routeId)
              val country = countryAnalyzer.relationCountry(relation)
              val name = relation.tags("note").getOrElse("no-name")
              Some(LoadedRoute(country, networkType, name, data, relation))
            case None =>
              log.warn(s"Route $routeId load at ${timestamp.iso} does not contain networkType (tag 'network'), continue processing\n---\n$xmlString\n---")
              None
          }
      }
    }
  }

  override def loadRoutes(timestamp: Timestamp, routeIds: Seq[Long]): Seq[LoadedRoute] = {

    val xmlString: String = log.elapsed {
      val xml = nonCachingExecutor.executeQuery(Some(timestamp), QueryRelations("routes", routeIds))
      (s"""Load routes ${routeIds.mkString(",")} at ${timestamp.iso}""", xml)
    }

    if (xmlString.isEmpty) {
      Seq()
    }
    else {
      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: Exception =>
          throw new RuntimeException(s"""Could not load routes ${routeIds.mkString(",")} at ${timestamp.iso}\n---\n$xmlString\n---""", e)
      }

      val rawData1 = new Parser().parse(xml.head)
      val rawData = rawData1.copy(timestamp = Some(timestamp))
      routeIds.flatMap { routeId =>
        rawData.relationWithId(routeId) match {
          case None =>
            log.warn(s"Could not find route $routeId in raw data at ${timestamp.iso}, assume route does not exist, continue processing\n---\n$xmlString\n---")

            throw new RuntimeException(s"Could not find route $routeId in raw data at ${timestamp.iso}, assume route does not exist, continue processing\n---\n$xmlString\n---")

            None

          case Some(rawRelation) =>
            rawRelation.tags("network").flatMap(NetworkType.withName) match {
              case Some(networkType) =>
                val data = new DataBuilder(networkType, rawData).data
                val relation = data.relations(routeId)
                val country = countryAnalyzer.relationCountry(relation)
                val name = relation.tags("note").getOrElse("no-name")
                Some(LoadedRoute(country, networkType, name, data, relation))
              case None =>
                log.warn(s"Route $routeId load at ${timestamp.iso} does not contain networkType (tag 'network'), continue processing\n---\n$xmlString\n---")
                None
            }
        }
      }
    }
  }

}
