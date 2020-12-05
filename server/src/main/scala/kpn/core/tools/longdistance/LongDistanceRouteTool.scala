package kpn.core.tools.longdistance

import kpn.api.common.Bounds
import kpn.api.common.longdistance.LongDistanceRoute
import kpn.api.common.longdistance.LongDistanceRouteSegment
import kpn.api.custom.Relation
import kpn.api.custom.Tags
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
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder
import kpn.server.repository.LongDistanceRouteRepositoryImpl
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonWriter

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
    Couch.executeIn("kpn-database", "analysis1") { database =>
      // new LongDistanceRouteTool(executor, database).tempWriteXmlFiles()
      new LongDistanceRouteTool(executor, database).analyzeRoutes()
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

    val gpxLineStringOption: Option[LineString] = routeDefinition.gpxFilename match {
      case Some(filename) => Some(readGpx(filename))
      case None => None
    }

    loadOsmData(routeDefinition) match {
      case None =>
      case Some(data) =>

        data.relations.get(routeDefinition.routeId) match {
          case None =>
            log.warn(s"Could not find route ${routeDefinition.routeId} in raw data")
            None

          case Some(routeRelation) => Some(routeRelation)

            val osmRouteSegments = toRouteSegments(routeRelation)

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
            val osmDistance = Math.round(osmRouteSegments.map(_.segment.meters).sum / 1000)
            // val distance = Math.round(routeRelation.wayMembers.map(_.way.length).sum / 1000)

            val gpxGeometry = gpxLineStringOption.map(lineString => toGeoJson(lineString))
            val okGeometry = okOption.map(geometry => toGeoJson(geometry))
            val nokGeometry = nokOption.map(geometry => toGeoJson(geometry))

            // TODO merge gpx bounds + ok + nok
            val bounds = mergeBounds(osmRouteSegments.map(_.segment.bounds))

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
              osmRouteSegments.map(_.segment),
              gpxGeometry,
              okGeometry,
              nokGeometry
            )
            routeRepository.save(route)
        }


    }
  }

  private def readGpx(filename: String): LineString = {
    new LongDistanceRouteGpxReader().read(filename)
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

  private def toGeoJson(geometry: Geometry): String = {
    new GeoJsonWriter().write(geometry).replaceAll("EPSG:0", "EPSG:4326")
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

  private def toRouteSegments(routeRelation: Relation): Seq[LongDistanceRouteSegmentData] = {

    val fragments: Seq[Fragment] = new FragmentAnalyzer(Seq(), routeRelation.wayMembers).fragments
    val fragmentsCopy = fragments.map { fragment => // temporary hack to remove paved/unpaved info
      val rawWayCopy = fragment.way.raw.copy(tags = Tags.empty)
      val wayCopy = fragment.way.copy(raw = rawWayCopy)
      fragment.copy(way = wayCopy)
    }
    val fragmentMap = fragmentsCopy.map(f => f.id -> f).toMap
    val fragmentIds = fragmentMap.values.map(_.id).toSet
    val segments = new SegmentBuilder(fragmentMap).segments(fragmentIds)

    segments.zipWithIndex.map { case (segment, index) =>

      val lineString = geomFactory.createLineString(segment.nodes.map(node => new Coordinate(node.lon, node.lat)).toArray)
      val meters: Long = Math.round(toMeters(lineString.getLength))
      val bounds = toBounds(lineString.getCoordinates.toSeq)
      val geoJson = toGeoJson(lineString)

      LongDistanceRouteSegmentData(
        LongDistanceRouteSegment(
          index + 1,
          meters,
          bounds,
          geoJson
        ),
        lineString
      )
    }
  }

  private def toBounds(coordinates: Seq[Coordinate]): Bounds = {
    val minLat = coordinates.map(_.getY).min
    val maxLat = coordinates.map(_.getY).max
    val minLon = coordinates.map(_.getX).min
    val maxLon = coordinates.map(_.getX).max
    new Bounds(
      minLat,
      minLon,
      maxLat,
      maxLon
    )
  }

  private def mergeBounds(boundss: Seq[Bounds]): Bounds = {
    val minLat = boundss.map(_.minLat).min
    val maxLat = boundss.map(_.maxLat).max
    val minLon = boundss.map(_.minLon).min
    val maxLon = boundss.map(_.maxLon).max
    new Bounds(
      minLat,
      minLon,
      maxLat,
      maxLon
    )
  }

}
