package kpn.server.analyzer.engine.monitor

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

  def analyze(routeLines: Seq[LineString], referenceLines: Seq[LineString]): MonitorRouteDeviationAnalysis = {

    val tree = buildRTree(routeLines)

    val analysisResults = analyzeDeviations(tree, referenceLines)
    val allMatches = geometryFactory.createGeometryCollection(analysisResults.map(_.matches).toArray)
    val referenceDistance = Math.round(referenceLines.map(Haversine.meters).sum)
    val matchesGeometry = Some(MonitorRouteAnalysisSupport.toGeoJson(allMatches))
    val deviations = organizeDeviations(analysisResults)

    MonitorRouteDeviationAnalysis(
      analysisResults,
      referenceDistance,
      matchesGeometry,
      deviations
    )
  }

  private def analyzeDeviations(tree: STRtree, referenceLines: Seq[LineString]): Seq[DeviationAnalysisResult] = {
    val referenceSegmentsSize = referenceLines.size
    referenceLines.zipWithIndex.map { case (referenceLine, index) =>
      Log.context(s"reference segment ${index + 1}/$referenceSegmentsSize") {
        analyzeReferenceLine(tree, referenceLine)
      }
    }
  }

  private def organizeDeviations(analysisResults: Seq[DeviationAnalysisResult]): Seq[MonitorRouteDeviation] = {
    analysisResults.flatMap(_.deviations).sortBy(_.distance).reverse.zipWithIndex.map { case (s, index) =>
      s.copy(id = index + 1)
    }
  }

  private def buildRTree(routeLines: Seq[LineString]): STRtree = {
    val tree = new STRtree(4)
    routeLines.foreach { line =>
      tree.insert(line.getEnvelopeInternal, line)
    }
    tree
  }

  private def analyzeReferenceLine(tree: STRtree, referenceLine: LineString): DeviationAnalysisResult = {
    val referenceSampleCoordinates = MonitorRouteAnalysisSupport.toSampleCoordinates(SampleDistanceMeters, referenceLine)
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
