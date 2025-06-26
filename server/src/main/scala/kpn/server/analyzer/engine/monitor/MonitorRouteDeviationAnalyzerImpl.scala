package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Way
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteDeviationAnalysis
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point
import org.locationtech.jts.index.strtree.STRtree
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.springframework.stereotype.Component

import scala.jdk.CollectionConverters.*

case class ReferenceCoordinateSequence(
  indexes: Seq[Int]
)

@Component
class MonitorRouteDeviationAnalyzerImpl extends MonitorRouteDeviationAnalyzer {

  private val geometryFactory = new GeometryFactory
  private val log = Log(classOf[MonitorRouteDeviationAnalyzer])

  private val SampleDistanceMeters = 10
  private val ToleranceMeters = 10

  def analyze(ways: Seq[Way], referenceGeoJson: String): MonitorRouteDeviationAnalysis = {

    val tree = buildRTree(ways)
    val referenceGeometry = new GeoJsonReader().read(referenceGeoJson)
    val referenceSegments = MonitorRouteReferenceUtil.toLineStrings(referenceGeometry)

    val analysisResults = analyzeReferenceSegments(tree, referenceSegments)
    val allMatches = geometryFactory.createGeometryCollection(analysisResults.map(_.matches).toArray)
    val referenceDistance = Math.round(referenceSegments.map(Haversine.meters).sum)
    val matchesGeometry = Some(MonitorRouteAnalysisSupport.toGeoJson(allMatches))
    val deviations = organizeDeviations(analysisResults)

    MonitorRouteDeviationAnalysis(
      analysisResults,
      referenceDistance,
      referenceGeoJson,
      matchesGeometry,
      deviations
    )
  }

  private def analyzeReferenceSegments(tree: STRtree, referenceSegments: Seq[LineString]): Seq[DeviationAnalysisResult] = {
    val referenceSegmentsSize = referenceSegments.size
    val analysisResults = referenceSegments.zipWithIndex.map { case (referenceSegment, index) =>
      Log.context(s"reference segment ${index + 1}/$referenceSegmentsSize") {
        analyzeReferenceSegment(tree, referenceSegment)
      }
    }
    analysisResults
  }

  private def organizeDeviations(analysisResults: Seq[DeviationAnalysisResult]) = {
    analysisResults.flatMap(_.deviations).sortBy(_.distance).reverse.zipWithIndex.map { case (s, index) =>
      s.copy(id = index + 1)
    }
  }

  private def buildRTree(ways: Seq[Way]): STRtree = {
    val tree = new STRtree(4)
    ways.foreach { way =>
      val linestring = geometryFactory.createLineString(
        way.nodes.map(node => new Coordinate(node.lon, node.lat)).toArray
      )
      tree.insert(linestring.getEnvelopeInternal, linestring)
    }
    tree
  }

  private def analyzeReferenceSegment(tree: STRtree, referenceSegment: LineString): DeviationAnalysisResult = {
    val referenceSampleCoordinates = MonitorRouteAnalysisSupport.toSampleCoordinates(SampleDistanceMeters, referenceSegment)
    val distances = analyzeDistances(tree, referenceSampleCoordinates)
    val (matchingSequences, deviationSequences) = calculateDeviations(distances, referenceSampleCoordinates)
    val matches = MonitorRouteAnalysisSupport.toMultiLineString(
      referenceSampleCoordinates,
      matchingSequences
    )
    val deviations = buildDeviations(deviationSequences, distances, referenceSampleCoordinates)
    DeviationAnalysisResult(deviations, matches)
  }

  private def buildDeviations(
    deviationSequences: Seq[ReferenceCoordinateSequence],
    distances: Vector[Double],
    referenceSampleCoordinates: Seq[Coordinate]
  ): Seq[MonitorRouteDeviation] = {
    deviationSequences.zipWithIndex.flatMap { case (sequence, sequenceIndex) =>
      val maxDistance = sequence.indexes.map(index => distances(index)).max
      val lineString = MonitorRouteAnalysisSupport.toLineString(referenceSampleCoordinates, sequence)
      val meters = Math.round(Haversine.meters(lineString))
      if (meters == 0L) {
        None
      }
      else {
        val bounds = MonitorRouteAnalysisSupport.toBounds(lineString.getCoordinates.toSeq)
        val geoJson = MonitorRouteAnalysisSupport.toGeoJson(lineString)
        Some(
          MonitorRouteDeviation(
            sequenceIndex + 1,
            meters,
            maxDistance.toLong,
            bounds,
            geoJson
          )
        )
      }
    }
  }

  private def calculateDeviations(
    distances: Vector[Double],
    referenceSampleCoordinates: Seq[Coordinate]
  ): (Seq[ReferenceCoordinateSequence], Seq[ReferenceCoordinateSequence]) = {
    val distanceWithinToleranceFlags = distances.map(distance => distance < ToleranceMeters)
    val flagsAndIndexes = distanceWithinToleranceFlags.zipWithIndex.map { case (flag, index) => flag -> index }
    val splittedFlagsAndIndexes = MonitorRouteAnalysisSupport.split(flagsAndIndexes.toList)
    val matchingSequences = splittedFlagsAndIndexes.filter(_.head._1).map(xx => ReferenceCoordinateSequence(xx.map(_._2)))
    val deviationSequences = splittedFlagsAndIndexes.filterNot(_.head._1).map(xx => ReferenceCoordinateSequence(xx.map(_._2)))
    (matchingSequences, deviationSequences)
  }

  private def analyzeDistances(tree: STRtree, referenceSampleCoordinates: Seq[Coordinate]): Vector[Double] = {
    log.infoElapsed {
      val distances = referenceSampleCoordinates.toVector.map { coordinate =>
        val point = geometryFactory.createPoint(coordinate)
        val lineStrings = findNearLineStrings(tree, coordinate)
        calculateMinimumDistance(point, lineStrings)
      }
      (s"distances (samples=${referenceSampleCoordinates.size})", distances)
    }
  }

  private def calculateMinimumDistance(point: Point, lineStrings: Seq[LineString]): Double = {
    val lineStringDistances = lineStrings.map(_.distance(point))
    if (lineStringDistances.nonEmpty) {
      val d = toMeters(lineStringDistances.min) // closest distance between reference point and osm route
      if (d > 2500) {
        2500 // long distance: (2500 means 2500m or more)
      }
      else {
        d
      }
    }
    else {
      2500 // no near line strings means long distance (2500 means 2500m or more)
    }
  }

  private def findNearLineStrings(tree: STRtree, coordinate: Coordinate): Seq[LineString] = {
    val envelope = new Envelope(coordinate)
    envelope.expandBy(.04, .02 /* distance in degrees */)
    val nearLineStrings = tree.query(envelope)
    nearLineStrings.asScala.map(_.asInstanceOf[LineString]).toSeq
  }
}
