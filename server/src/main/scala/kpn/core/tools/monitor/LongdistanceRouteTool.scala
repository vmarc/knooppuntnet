package kpn.core.tools.monitor

import kpn.api.common.monitor.LongdistanceRoute
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.core.tools.monitor.LongdistanceRouteTool.MonitorRouteDefinition
import kpn.core.util.Log
import kpn.server.repository.LongdistanceRouteRepositoryImpl
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.LineString

import java.io.File
import java.nio.charset.Charset
import scala.xml.XML

object LongdistanceRouteTool {

  case class MonitorRouteDefinition(routeId: Long, gpxFilename: Option[String])

  val routeDefinitions: Seq[MonitorRouteDefinition] = Seq(
    MonitorRouteDefinition(3121667L, None), // GR5 Flanders
    MonitorRouteDefinition(3121668L, Some("GR005_Parcours-principal_2020-07-11.gpx")), // GR5 Wallonia
    MonitorRouteDefinition(5951316L, None), // GR5 Variant Genk
    MonitorRouteDefinition(2907324L, None), //  GR5 Variant Hasselt
    MonitorRouteDefinition(2923308L, None), // GR5 Variant Testelt-Zichem (Demervariant)
    MonitorRouteDefinition(13658L, None), //   GR5A Noord
    MonitorRouteDefinition(2629186L, None), // GR5A Zuid
    MonitorRouteDefinition(133437L, None), //  GR5A Rond Canisvlietsche Kreek
    MonitorRouteDefinition(13638L, None), //   GR5A South: Variant E2 / Verbinding GR5A-GR12
    MonitorRouteDefinition(6481535L, None), // GR5A GR5A South: Variant Dode Ijzer
    MonitorRouteDefinition(5556328L, None), // GR5A GR5A South: Connection to GR123
    MonitorRouteDefinition(3194117L, None), // GR5A GR5A Noord: Variant Oostende Haven
    MonitorRouteDefinition(2929186L, Some("GR014_Parcours-principal_2020-06-26.gpx")), // GR14
    MonitorRouteDefinition(8613893L, Some("GR015_Parcours-principal_2019-12-05.gpx")), // GR15
    MonitorRouteDefinition(197843L, Some("GR016_Parcours-principal_2020-09-10.gpx")), //  GR16
    MonitorRouteDefinition(2067765L, Some("routeyou-gr128.gpx")) // GR128
  )

  def main(args: Array[String]): Unit = {
    val executor = new OverpassQueryExecutorImpl()
    Couch.executeIn("kpn-database", "analysis1") { analysisDatabase =>
      new LongdistanceRouteTool(executor, analysisDatabase).analyzeRoutes()
    }
  }
}

class LongdistanceRouteTool(
  overpassQueryExecutor: OverpassQueryExecutor,
  analysisDatabase: Database
) {

  import LongdistanceRouteAnalyzer._

  private val log = Log(classOf[LongdistanceRouteTool])
  private val routeRepository = new LongdistanceRouteRepositoryImpl(analysisDatabase)

  def tempWriteXmlFiles(): Unit = {
    LongdistanceRouteTool.routeDefinitions.foreach { routeDefinition =>
      Log.context(s"${routeDefinition.routeId}") {
        val xmlString: String = log.elapsed {
          val xml = overpassQueryExecutor.executeQuery(None, QueryRelation(routeDefinition.routeId))
          (s"Load route ${routeDefinition.routeId}", xml)
        }
        FileUtils.writeStringToFile(new File(s"/kpn/gpx/${routeDefinition.routeId}.xml"), xmlString, Charset.forName("UTF-8"))
      }
    }
  }

  def analyzeRoutes(): Unit = {
    LongdistanceRouteTool.routeDefinitions.foreach { routeDefinition =>
      Log.context(s"${routeDefinition.routeId}") {
        log.info(s"Analyze ${routeDefinition.routeId}")
        analyzeRoute(routeDefinition)
      }
    }
    log.info("Done")
  }

  private def analyzeRoute(routeDefinition: MonitorRouteDefinition): Unit = {

    loadOsmData(routeDefinition) match {
      case None =>
      case Some(data) =>

        data.relations.get(routeDefinition.routeId) match {
          case None =>
            log.warn(s"Could not find route ${routeDefinition.routeId} in raw data")
            None

          case Some(routeRelation) =>

            val osmRouteSegments = toRouteSegments(routeRelation)

            val gpxLineStringOption: Option[LineString] = routeDefinition.gpxFilename match {
              case None => None
              case Some(filename) => Some(readGpx(filename))
            }

            gpxLineStringOption match {
              case None =>
                val osmDistance = Math.round(osmRouteSegments.map(_.segment.meters).sum / 1000)
                val bounds = mergeBounds(osmRouteSegments.map(_.segment.bounds))
                val route = LongdistanceRoute(
                  routeRelation.id,
                  routeRelation.tags("ref"),
                  routeRelation.tags("name").getOrElse(s"$routeRelation.id"),
                  routeRelation.tags("name:nl"),
                  routeRelation.tags("name:en"),
                  routeRelation.tags("name:de"),
                  routeRelation.tags("name:fr"),
                  None,
                  routeRelation.tags("operator"),
                  routeRelation.tags("website"),
                  routeRelation.wayMembers.size,
                  osmDistance,
                  0,
                  bounds,
                  None,
                  osmRouteSegments.map(_.segment),
                  None,
                  None,
                  Seq.empty
                )
                routeRepository.save(route)

              case Some(gpxLineString) =>
                val route = LongdistanceRouteAnalyzer.analyze(routeDefinition.gpxFilename.get, gpxLineString, routeRelation, osmRouteSegments)
                routeRepository.save(route)
            }
        }
    }
  }

  private def readGpx(filename: String): LineString = {
    new MonitorRouteGpxReader().read(filename)
  }

  private def readRouteRelationXml(routeDefinition: MonitorRouteDefinition): String = {
    // val xmlString: String = log.elapsed {
    //   val xml = overpassQueryExecutor.executeQuery(None, QueryRelation(routeDefinition.routeId))
    //   (s"Load route ${routeDefinition.routeId}", xml)
    // }
    FileUtils.readFileToString(new File(s"/kpn/gpx/${routeDefinition.routeId}.xml"), Charset.forName("UTF-8"))
  }

  private def loadOsmData(routeDefinition: MonitorRouteDefinition): Option[Data] = {
    val xmlString = readRouteRelationXml(routeDefinition)
    if (xmlString.isEmpty) {
      log.warn(s"Could not load route ${routeDefinition.routeId}")
      None
    }
    else {
      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: Exception =>
          throw new RuntimeException(s"Could not load route ${routeDefinition.routeId}\n---\n$xmlString\n---", e)
      }

      val rawData = new Parser().parse(xml.head)
      Some(new DataBuilder(rawData).data)
    }
  }

}
