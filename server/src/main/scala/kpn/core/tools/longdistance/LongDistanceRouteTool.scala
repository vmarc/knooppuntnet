package kpn.core.tools.longdistance

import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.core.tools.longdistance.LongDistanceRouteTool.LongDistanceRouteDefinition
import kpn.core.util.Log
import kpn.server.repository.LongDistanceRouteRepositoryImpl
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.LineString

import java.io.File
import java.nio.charset.Charset
import scala.xml.XML

object LongDistanceRouteTool {

  case class LongDistanceRouteDefinition(routeId: Long, gpxFilename: Option[String])

  val routeDefinitions: Seq[LongDistanceRouteDefinition] = Seq(
    LongDistanceRouteDefinition(3121667L, None), // GR5 Flanders
    LongDistanceRouteDefinition(3121668L, Some("GR005_Parcours-principal_2020-07-11.gpx")), // GR5 Wallonia
    LongDistanceRouteDefinition(5951316L, None), // GR5 Variant Genk
    LongDistanceRouteDefinition(2907324L, None), //  GR5 Variant Hasselt
    LongDistanceRouteDefinition(2923308L, None), // GR5 Variant Testelt-Zichem (Demervariant)
    LongDistanceRouteDefinition(13658L, None), //   GR5A Noord
    LongDistanceRouteDefinition(2629186L, None), // GR5A Zuid
    LongDistanceRouteDefinition(133437L, None), //  GR5A Rond Canisvlietsche Kreek
    LongDistanceRouteDefinition(13638L, None), //   GR5A South: Variant E2 / Verbinding GR5A-GR12
    LongDistanceRouteDefinition(6481535L, None), // GR5A GR5A South: Variant Dode Ijzer
    LongDistanceRouteDefinition(5556328L, None), // GR5A GR5A South: Connection to GR123
    LongDistanceRouteDefinition(3194117L, None), // GR5A GR5A Noord: Variant Oostende Haven
    LongDistanceRouteDefinition(2929186L, Some("GR014_Parcours-principal_2020-06-26.gpx")), // GR14
    LongDistanceRouteDefinition(8613893L, Some("GR015_Parcours-principal_2019-12-05.gpx")), // GR15
    LongDistanceRouteDefinition(197843L, Some("GR016_Parcours-principal_2020-09-10.gpx")), //  GR16
    LongDistanceRouteDefinition(2067765L, None) // GR128
  )

  def main(args: Array[String]): Unit = {
    val executor = new OverpassQueryExecutorImpl()
    Couch.executeIn("kpn-database", "long-distance") { longDistanceRouteChangeDatabase =>
      Couch.executeIn("kpn-database", "analysis1") { analysisDatabase =>
        new LongDistanceRouteTool(executor, analysisDatabase, longDistanceRouteChangeDatabase).analyzeRoutes()
      }
    }
  }
}

class LongDistanceRouteTool(
  overpassQueryExecutor: OverpassQueryExecutor,
  analysisDatabase: Database,
  longDistanceRouteChangeDatabase: Database
) {

  import LongDistanceRouteAnalyzer._

  private val log = Log(classOf[LongDistanceRouteTool])
  private val routeRepository = new LongDistanceRouteRepositoryImpl(analysisDatabase, longDistanceRouteChangeDatabase)

  def tempWriteXmlFiles(): Unit = {
    LongDistanceRouteTool.routeDefinitions.foreach { routeDefinition =>
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
    LongDistanceRouteTool.routeDefinitions.foreach { routeDefinition =>
      Log.context(s"${routeDefinition.routeId}") {
        log.info(s"Analyze ${routeDefinition.routeId}")
        analyzeRoute(routeDefinition)
      }
    }
    log.info("Done")
  }

  private def analyzeRoute(routeDefinition: LongDistanceRouteDefinition): Unit = {

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
              case _ =>
              case Some(gpxLineString) =>
                val route = LongDistanceRouteAnalyzer.analyze(routeDefinition.gpxFilename.get, gpxLineString, routeRelation, osmRouteSegments)
                routeRepository.save(route)
            }

        }
    }
  }

  private def readGpx(filename: String): LineString = {
    new LongDistanceRouteGpxReader().read(filename)
  }

  private def readRouteRelationXml(routeDefinition: LongDistanceRouteDefinition): String = {
    // val xmlString: String = log.elapsed {
    //   val xml = overpassQueryExecutor.executeQuery(None, QueryRelation(routeDefinition.routeId))
    //   (s"Load route ${routeDefinition.routeId}", xml)
    // }
    FileUtils.readFileToString(new File(s"/kpn/gpx/${routeDefinition.routeId}.xml"), Charset.forName("UTF-8"))
  }

  private def loadOsmData(routeDefinition: LongDistanceRouteDefinition): Option[Data] = {
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
