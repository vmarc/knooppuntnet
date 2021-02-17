package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.load.data.LoadedRoute
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class RouteLoaderImpl(
  cachingOverpassQueryExecutor: OverpassQueryExecutor,
  countryAnalyzer: CountryAnalyzer
) extends RouteLoader {

  private val log = Log(classOf[RouteLoaderImpl])

  override def loadRoute(timestamp: Timestamp, routeId: Long): Option[LoadedRoute] = {
    log.info("Load route " + routeId)
    try {
      doLoadRoute(timestamp, routeId)
    }
    catch {
      case e: Throwable =>
        val message = s"Exception loading route (routeId=$routeId)"
        log.error(message, e)
        throw e
    }
  }

  private def doLoadRoute(timestamp: Timestamp, routeId: Long): Option[LoadedRoute] = {

    val xmlString: String = log.elapsed {
      val xml = cachingOverpassQueryExecutor.executeQuery(Some(timestamp), QueryRelation(routeId))
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
          RelationAnalyzer.scopedNetworkType(rawRelation) match {
            case Some(scopedNetworkType) =>
              val data = new DataBuilder(rawData).data
              val relation = data.relations(routeId)
              val country = countryAnalyzer.relationCountry(relation)
              val name = relation.tags("note").getOrElse("no-name")
              Some(LoadedRoute(country, scopedNetworkType, name, data, relation))
            case None =>
              log.warn(s"Route $routeId load at ${timestamp.iso} does not contain networkType (tag 'network'), continue processing\n---\n$xmlString\n---")
              None
          }
      }
    }
  }
}
