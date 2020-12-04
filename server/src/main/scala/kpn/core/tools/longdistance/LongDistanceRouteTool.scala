package kpn.core.tools.longdistance

import java.io.File
import java.nio.charset.Charset

import kpn.api.common.Bounds
import kpn.api.common.longdistance.LongDistanceRoute
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder
import kpn.server.repository.LongDistanceRouteRepositoryImpl
import org.apache.commons.io.FileUtils
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonWriter

import scala.xml.XML

object LongDistanceRouteTool {

  case class LongDistanceRouteDefinition(routeId: Long, gpxFilename: Option[String])

  val routeDefinitions: Seq[LongDistanceRouteDefinition] = Seq(
    LongDistanceRouteDefinition(3121667L, Some("GR005_Parcours-principal_2020-07-11.gpx")), // GR5 Flanders
    LongDistanceRouteDefinition(3121668L, None), // GR5 Wallonia
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
    Couch.executeIn("kpn-database", "analysis1") { database =>
      // new LongDistanceRouteTool(executor, database).tempWriteXmlFiles()
      new LongDistanceRouteTool(executor, database).generateRoutes()
    }
  }
}

class LongDistanceRouteTool(overpassQueryExecutor: OverpassQueryExecutor, database: Database) {

  private val log = Log(classOf[LongDistanceRouteTool])
  private val routeRepository = new LongDistanceRouteRepositoryImpl(database)
  private val geomFactory = new GeometryFactory
  private val sampleDistanceMeters = 10
  private val toleranceMeters = 10

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

  def generateRoutes(): Unit = {

    LongDistanceRouteTool.routeDefinitions.foreach { routeDefinition =>

      Log.context(s"${routeDefinition.routeId}") {

        val gpxLineStringOption: Option[LineString] = routeDefinition.gpxFilename match {
          case Some(filename) => Some(readGpx(filename))
          case None => None
        }

        //        val xmlString: String = log.elapsed {
        //          val xml = overpassQueryExecutor.executeQuery(None, QueryRelation(routeDefinition.routeId))
        //          (s"Load route ${routeDefinition.routeId}", xml)
        //        }
        val xmlString = FileUtils.readFileToString(new File(s"/kpn/gpx/${routeDefinition.routeId}.xml"), Charset.forName("UTF-8"))

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
          val data = new DataBuilder(rawData).data

          data.relations.get(routeDefinition.routeId) match {
            case None =>
              log.warn(s"Could not find route ${routeDefinition.routeId} in raw data, assume route does not exist, continue processing\n---\n$xmlString\n---")
              None
            case Some(routeRelation) => Some(routeRelation)

              val fragments: Seq[Fragment] = new FragmentAnalyzer(Seq(), routeRelation.wayMembers).fragments
              val fragmentsCopy = fragments.map { fragment => // temporary hack to remove paved/unpaved info
                val rawWayCopy = fragment.way.raw.copy(tags = Tags.empty)
                val wayCopy = fragment.way.copy(raw = rawWayCopy)
                fragment.copy(way = wayCopy)
              }
              val fragmentMap = fragmentsCopy.map(f => f.id -> f).toMap
              val fragmentIds = fragmentMap.values.map(_.id).toSet
              val segments = new SegmentBuilder(fragmentMap).segments(fragmentIds)
              val segmentLineStrings = segments.map { segment =>
                geomFactory.createLineString(segment.nodes.map(node => new Coordinate(node.lat, node.lon)).toArray)
              }
              val osmMultiLineString = geomFactory.createMultiLineString(segmentLineStrings.toArray)

              //              val osmLineString = geomFactory.createLineString(coordinates.toArray)
//              val osmLineString = gpxLineStringOption.get

              val (okOption: Option[MultiLineString], nokOption: Option[MultiLineString]) = gpxLineStringOption match {
                case None => (None, None)
                case Some(gpxLineString) =>
                  (None, None)
//                  val distanceBetweenSamples = sampleDistanceMeters.toDouble * osmLineString.getLength / toMeters(osmLineString.getLength)
//                  val densifiedOsm = Densifier.densify(osmLineString, distanceBetweenSamples)
//                  val sampleCoordinates = densifiedOsm.getCoordinates.toSeq
//
//                  val distances = sampleCoordinates.toList.map(coordinate => toMeters(gpxLineString.distance(geomFactory.createPoint(coordinate))))
//
//                  log.info(s"distance max=${distances.max}")
//                  log.info(s"distance min=${distances.min}")
//
//                  val withinTolerance = distances.map(distance => distance < toleranceMeters)
//                  val okAndIndexes = withinTolerance.zipWithIndex.map { case (ok, index) => ok -> index }
//                  val splittedOkAndIndexes = split(okAndIndexes)
//
//                  val ok: MultiLineString = toMultiLineString(sampleCoordinates, splittedOkAndIndexes.filter(_.head._1))
//                  val nok = toMultiLineString(sampleCoordinates, splittedOkAndIndexes.filterNot(_.head._1))
//                  (Some(ok), Some(nok))
              }


              val gpxDistance = Math.round(toMeters(gpxLineStringOption.map(_.getLength).getOrElse(0)) / 1000)
              val osmDistance = Math.round(toMeters(osmMultiLineString.getLength) / 1000)
              // val distance = Math.round(routeRelation.wayMembers.map(_.way.length).sum / 1000)

              val osmGeometry: Option[String] = Some(new GeoJsonWriter().write(osmMultiLineString))
              val gpxGeometry: Option[String] = gpxLineStringOption.map(lineString => new GeoJsonWriter().write(lineString))
              val okGeometry: Option[String] = okOption.map(geometry => new GeoJsonWriter().write(geometry))
              val nokGeometry: Option[String] = nokOption.map(geometry => new GeoJsonWriter().write(geometry))

              val aa: Seq[Coordinate] = osmMultiLineString.getCoordinates.toSeq
              val bb: Seq[Coordinate] = gpxLineStringOption.toSeq.flatMap(_.getCoordinates.toSeq)
//              val cc: Seq[Coordinate] = okOption.toSeq.flatMap(_.getCoordinates.toSeq)
//              val dd: Seq[Coordinate] = nokOption.toSeq.flatMap(_.getCoordinates.toSeq)
              val allCoordinates: Seq[Coordinate] = aa ++ bb // ++ cc ++ dd

              val minLat = allCoordinates.map(_.getX).min
              val maxLat = allCoordinates.map(_.getX).max
              val minLon = allCoordinates.map(_.getY).min
              val maxLon = allCoordinates.map(_.getY).max

              val bounds = new Bounds(
                minLat, // 51.46190629340708,
                minLon, // 4.482781039550901,
                maxLat, // 51.480807485058904,
                maxLon //4.533901931717992
              )

              log.info(s"bounds = $bounds")

              val route = LongDistanceRoute(
                routeDefinition.routeId,
                routeRelation.tags("ref"),
                routeRelation.tags("name").getOrElse(s"$routeDefinition.routeId"),
                routeRelation.tags("name:nl"),
                routeRelation.tags("name:en"),
                routeRelation.tags("name:de"),
                routeRelation.tags("name:fr"),
                None,
                routeRelation.tags("operator"),
                routeRelation.tags("website"),
                routeRelation.wayMembers.size,
                osmDistance,
                gpxDistance,
                bounds,
                routeDefinition.gpxFilename,
                osmGeometry,
                gpxGeometry,
                okGeometry,
                nokGeometry
              )
              routeRepository.save(route)
          }
        }
      }
    }
    log.info("Done")
  }

  private def readGpx(filename: String): LineString = {

    val xml = XML.loadFile(new File("/kpn/gpx/" + filename))
    val tracks = (xml \ "trk").map { trk =>
      (trk \ "trkseg").map { trkseg =>
        val coordinates = (trkseg \ "trkpt").map { trkpt =>
          val lat = (trkpt \ "@lat").text
          val lon = (trkpt \ "@lon").text
          new Coordinate(lon.toDouble, lat.toDouble)
        }
        geomFactory.createLineString(coordinates.toArray)
      }
    }
    if (tracks.size != 1) {
      throw new RuntimeException("Unexpected number of tracks in gpx file: " + tracks.size)
    }
    val track = tracks.head
    if (track.size != 1) {
      throw new RuntimeException("Unexpected number of track segments in gpx file: " + track.size)
    }
    track.head
  }

  private def toMeters(value: Double): Double = {
    value * (math.Pi / 180) * 6378137
  }

  private def split(list: List[(Boolean, Int)]): List[List[(Boolean, Int)]] = {
    list match {
      case Nil => Nil
      case head :: tail =>
        val segment = list.takeWhile(_._1 == head._1)
        segment +: split(list.drop(segment.length))
    }
  }

  private def toMultiLineString(sampleCoordinates: Seq[Coordinate], segments: List[List[(Boolean, Int)]]) = {
    geomFactory.createMultiLineString(segments.map(segment => toLineString(sampleCoordinates, segment)).toArray)
  }

  private def toLineString(osmCoordinates: Seq[Coordinate], segment: List[(Boolean, Int)]): LineString = {
    val indexes = segment.map(_._2)
    val coordinates = indexes.map(index => osmCoordinates(index))
    geomFactory.createLineString(coordinates.toArray)
  }

}
