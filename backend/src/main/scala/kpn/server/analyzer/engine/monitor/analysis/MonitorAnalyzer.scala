package kpn.server.analyzer.engine.monitor.analysis

import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteAnalysisSupport.toLineString
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteAnalysisSupport.toMeters
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point
import org.locationtech.jts.index.strtree.STRtree

import scala.jdk.CollectionConverters.*

object MonitorAnalyzer {

  private val geometryFactory = new GeometryFactory
  private val log = Log(classOf[MonitorAnalyzer])

  private val SampleDistanceMeters = 10
  private val ToleranceMeters = 10

  /*
    Find the segments of the left lines that have a rightline within tolerance.
   */
  def analyze(leftLines: Seq[LineString], rightLines: Seq[LineString]): MonitorAnalysisResult = {
    val rightLinesTree = buildRTree(rightLines)
    val analysisResults = analyzeDeviations(leftLines, rightLinesTree)
    MonitorAnalysisResult(
      analysisResults.flatMap(_.matchesLines),
      analysisResults.flatMap(_.deviationLines),
    )
  }

  private def analyzeDeviations(leftLines: Seq[LineString], rightLinesTree: STRtree): Seq[MonitorAnalysisResult] = {
    val leftLinesSize = leftLines.size
    leftLines.zipWithIndex.map { case (leftLine, index) =>
      Log.context(s"left line ${index + 1}/$leftLinesSize") {
        analyzeLeftLine(leftLine, rightLinesTree)
      }
    }
  }

  private def buildRTree(rightLines: Seq[LineString]): STRtree = {
    val tree = new STRtree(4)
    rightLines.foreach { line =>
      tree.insert(line.getEnvelopeInternal, line)
    }
    tree
  }

  private def analyzeLeftLine(leftLine: LineString, rightLinesTree: STRtree): MonitorAnalysisResult = {
    val leftLineSampleCoordinates = LineSampler.toSampleCoordinates(SampleDistanceMeters, leftLine)
    val distances = calculateDistances(leftLineSampleCoordinates, rightLinesTree)
    val (matchingSequences, deviationSequences) = calculateDeviations(distances, leftLineSampleCoordinates)
    val matchingLines = matchingSequences.map(sequence => toLineString(leftLineSampleCoordinates, sequence))
    val deviations = buildDeviations(deviationSequences, distances, leftLineSampleCoordinates)
    MonitorAnalysisResult(
      matchingLines,
      deviations
    )
  }

  private def buildDeviations(
    deviationSequences: Seq[ReferenceCoordinateSequence],
    distances: Vector[Double],
    leftLineSampleCoordinates: Seq[Coordinate]
  ): Seq[DistanceLineString] = {
    deviationSequences.zipWithIndex.flatMap { case (sequence, sequenceIndex) =>
      val maxDistance = sequence.indexes.map(index => distances(index)).max.toLong
      val lineString = MonitorRouteAnalysisSupport.toLineString(leftLineSampleCoordinates, sequence)
      val meters = Math.round(Haversine.meters(lineString))
      if (meters > 0L) {
        Some(DistanceLineString(maxDistance, lineString))
      }
      else {
        None
      }
    }
  }

  private def calculateDeviations(
    distances: Vector[Double],
    referenceSampleCoordinates: Seq[Coordinate]
  ): (Seq[ReferenceCoordinateSequence], Seq[ReferenceCoordinateSequence]) = {
    val distanceWithinToleranceFlags = buildFlags(distances)
    val flagsAndIndexes = distanceWithinToleranceFlags.zipWithIndex.map { case (flag, index) => flag -> index }
    split(flagsAndIndexes)
  }

  /*
    Divides the reference into segments where each segment is either entirely within tolerance
    or entirely outside tolerance.
   */
  private def split(flagsAndIndexes: Vector[(Boolean, Int)]): (Seq[ReferenceCoordinateSequence], Seq[ReferenceCoordinateSequence]) = {
    val splittedFlagsAndIndexes = MonitorRouteAnalysisSupport.split(flagsAndIndexes.toList)
    val matchingSequences = filterSequences(splittedFlagsAndIndexes, flag = true)
    val deviationSequences = filterSequences(splittedFlagsAndIndexes, flag = false)
    (matchingSequences, deviationSequences)
  }

  private def filterSequences(list: List[List[(Boolean, Int)]], flag: Boolean): Seq[ReferenceCoordinateSequence] = {
    list.filter(_.head._1 == flag).map(flagAndIndex => ReferenceCoordinateSequence(flagAndIndex.map(_._2)))
  }

  /*
   Builds a vector of boolean flags that indicate whether each distance is within the tolerance threshold.
   `true` means the distance is acceptable (within tolerance), `false` means it deviates too much.
  */
  private def buildFlags(distances: Vector[Double]) = {
    distances.map(distance => distance < ToleranceMeters)
  }

  private def calculateDistances(leftSampleCoordinates: Seq[Coordinate], rightLinesTree: STRtree): Vector[Double] = {
    log.debugElapsed {
      val distances = leftSampleCoordinates.toVector.map { coordinate =>
        val point = geometryFactory.createPoint(coordinate)
        val lineStrings = findNearLineStrings(rightLinesTree, coordinate)
        calculateMinimumDistance(point, lineStrings)
      }
      (s"distances (samples=${leftSampleCoordinates.size})", distances)
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

  private def findNearLineStrings(rightLinesTree: STRtree, coordinate: Coordinate): Seq[LineString] = {
    val envelope = new Envelope(coordinate)
    envelope.expandBy(.04, .02 /* distance in degrees */)
    val nearLineStrings = rightLinesTree.query(envelope)
    nearLineStrings.asScala.map(_.asInstanceOf[LineString]).toSeq
  }
}

class MonitorAnalyzer
