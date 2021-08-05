package kpn.server.analyzer.load

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelations
import kpn.core.util.Log
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class RouteLoaderImpl(
  nonCachingOverpassQueryExecutor: OverpassQueryExecutor
) extends RouteLoader {

  private val log = Log(classOf[RouteLoaderImpl])

  override def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Relation] = {
    if (routeIds.isEmpty) {
      Seq.empty
    }
    else {
      val xmlString: String = log.elapsed {
        val query = QueryRelations(routeIds)
        val xml = nonCachingOverpassQueryExecutor.executeQuery(Some(timestamp), query)
        (s"Load ${routeIds.size} routes at ${timestamp.iso}", xml)
      }

      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: Exception =>
          throw new RuntimeException(s"Could not load routes at ${timestamp.iso}", e)
      }

      val rawData = new Parser().parse(xml.head)
      val data = new DataBuilder(rawData).data
      routeIds.map { routeId =>
        data.relations(routeId)
      }
    }
  }
}
