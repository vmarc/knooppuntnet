package kpn.core.tools.monitor

import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysis
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer.toMeters
import kpn.server.analyzer.engine.monitor.MonitorRouteSegmentData
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonReader

class MonitorDemoAnalyzer() {

  private val geomFactory = new GeometryFactory
  private val sampleDistanceMeters = 10
  private val toleranceMeters = 10
  private val log = Log(classOf[MonitorDemoAnalyzer])

  def analyze(routeReference: MonitorRouteReference, routeRelation: Relation, now: Timestamp): MonitorRouteState = {

    val routeSegments = MonitorRouteAnalyzer.toRouteSegments(routeRelation)
    val routeAnalysis = analyzeChange(routeReference, routeRelation, routeSegments)

    MonitorRouteState(
      routeReference.monitorRouteId,
      routeRelation.id,
      now,
      routeAnalysis.wayCount,
      routeAnalysis.osmDistance,
      routeAnalysis.gpxDistance,
      routeAnalysis.bounds,
      Some(routeReference.key),
      routeAnalysis.osmSegments,
      routeAnalysis.okGeometry,
      routeAnalysis.nokSegments
    )
  }

  private def analyzeChange(reference: MonitorRouteReference, routeRelation: Relation, osmRouteSegments: Seq[MonitorRouteSegmentData]): MonitorRouteAnalysis = {

    val gpxLineString = new GeoJsonReader().read(reference.geometry)

    val (okOption: Option[MultiLineString], nokSegments: Seq[MonitorRouteNokSegment]) = {

      val distanceBetweenSamples = sampleDistanceMeters.toDouble * gpxLineString.getLength / MonitorRouteAnalyzer.toMeters(gpxLineString.getLength)
      val densifiedGpx = Densifier.densify(gpxLineString, distanceBetweenSamples)
      val sampleCoordinates = densifiedGpx.getCoordinates.toSeq

      val distances = sampleCoordinates.toList.map { coordinate =>
        val point = geomFactory.createPoint(coordinate)
        toMeters(osmRouteSegments.map(segment => segment.lineString.distance(point)).min)
      }

      val withinTolerance = distances.map(distance => distance < toleranceMeters)
      val okAndIndexes = withinTolerance.zipWithIndex.map { case (ok, index) => ok -> index }
      val splittedOkAndIndexes = MonitorRouteAnalyzer.split(okAndIndexes)

      val ok: MultiLineString = MonitorRouteAnalyzer.toMultiLineString(sampleCoordinates, splittedOkAndIndexes.filter(_.head._1))

      val noks = splittedOkAndIndexes.filterNot(_.head._1)

      val nok = noks.zipWithIndex.map { case (segment, segmentIndex) =>
        val segmentIndexes = segment.map(_._2)
        val maxDistance = distances.zipWithIndex.filter { case (distance, index) =>
          segmentIndexes.contains(index)
        }.map { case (distance, index) =>
          distance
        }.max

        val lineString = MonitorRouteAnalyzer.toLineString(sampleCoordinates, segment)
        val meters: Long = Math.round(toMeters(lineString.getLength))
        val bounds = MonitorRouteAnalyzer.toBounds(lineString.getCoordinates.toSeq)
        val geoJson = MonitorRouteAnalyzer.toGeoJson(lineString)

        MonitorRouteNokSegment(
          segmentIndex + 1,
          meters,
          maxDistance.toLong,
          bounds,
          geoJson
        )
      }

      val routeNokSegments: Seq[MonitorRouteNokSegment] = nok.sortBy(_.distance).reverse.zipWithIndex.map { case (s, index) =>
        s.copy(id = index + 1)
      }

      (Some(ok), routeNokSegments)
    }

    val gpxDistance = Math.round(toMeters(gpxLineString.getLength / 1000))
    val osmDistance = Math.round(osmRouteSegments.map(_.segment.meters).sum.toDouble / 1000)

    val gpxGeometry = MonitorRouteAnalyzer.toGeoJson(gpxLineString)
    val okGeometry = okOption.map(geometry => MonitorRouteAnalyzer.toGeoJson(geometry))

    // TODO merge gpx bounds + ok
    val bounds = Util.mergeBounds(osmRouteSegments.map(_.segment.bounds) ++ nokSegments.map(_.bounds))

    MonitorRouteAnalysis(
      routeRelation,
      routeRelation.wayMembers.size,
      osmDistance,
      gpxDistance,
      bounds,
      osmRouteSegments.map(_.segment),
      Some(gpxGeometry),
      okGeometry,
      nokSegments
    )
  }
}