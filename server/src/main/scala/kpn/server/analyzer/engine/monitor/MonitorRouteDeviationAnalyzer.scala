package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Way
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteDeviationAnalysis
import kpn.server.api.monitor.domain.MonitorRouteReference
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.index.strtree.STRtree
import org.locationtech.jts.io.geojson.GeoJsonReader

case class ReferenceCoordinateSequence(
  indexes: Seq[Int]
)

class MonitorRouteDeviationAnalyzer() {

  private val geomFactory = new GeometryFactory
  private val sampleDistanceMeters = 10
  private val toleranceMeters = 10
  private val log = Log(classOf[MonitorRouteDeviationAnalyzer])

  def analyze(ways: Seq[Way], reference: MonitorRouteReference): MonitorRouteDeviationAnalysis = {

    val tree = buildRTree(ways)
    val referenceGeoJson = new GeoJsonReader().read(reference.geometry)
    val referenceSegments = MonitorRouteReferenceUtil.toLineStrings(referenceGeoJson)

    val analysisResults = referenceSegments.zipWithIndex.map { case (referenceSegment, index) =>
      Log.context(s"reference segment ${index + 1}/${referenceSegments.size}") {
        analyzeReferenceSegment(tree, referenceSegment)
      }
    }

    val allMatches = geomFactory.createGeometryCollection(analysisResults.map(_.matches).toArray)

    val referenceDistance = Math.round(toMeters(referenceSegments.map(_.getLength).sum / 1000))

    val referenceGeometry = MonitorRouteAnalysisSupport.toGeoJson(referenceGeoJson)
    val matchesGeometry = Some(MonitorRouteAnalysisSupport.toGeoJson(allMatches))

    val deviations = analysisResults.flatMap(_.deviations).sortBy(_.distance).reverse.zipWithIndex.map { case (s, index) =>
      s.copy(id = index + 1)
    }

    MonitorRouteDeviationAnalysis(
      analysisResults,
      referenceDistance,
      referenceGeometry,
      matchesGeometry,
      deviations
    )
  }

  private def buildRTree(ways: Seq[Way]): STRtree = {
    val tree = new STRtree(4)
    ways.foreach { way =>
      val linestring = geomFactory.createLineString(
        way.nodes.map(node => new Coordinate(node.lon, node.lat)).toArray
      )
      tree.insert(linestring.getEnvelopeInternal, linestring)
    }
    tree
  }

  private def analyzeReferenceSegment(tree: STRtree, referenceSegment: LineString): DeviationAnalysisResult = {

    val referenceSampleCoordinates = MonitorRouteAnalysisSupport.toSampleCoordinates(sampleDistanceMeters, referenceSegment)
    val distances = analyzeDistances(tree, referenceSampleCoordinates)

    val distanceWithinToleranceFlags = distances.map(distance => distance < toleranceMeters)
    val flagsAndIndexes = distanceWithinToleranceFlags.zipWithIndex.map { case (flag, index) => flag -> index }
    val splittedFlagsAndIndexes = MonitorRouteAnalysisSupport.split(flagsAndIndexes.toList)
    val okSequences = splittedFlagsAndIndexes.filter(_.head._1).map(xx => ReferenceCoordinateSequence(xx.map(_._2)))
    val nokSequences = splittedFlagsAndIndexes.filterNot(_.head._1).map(xx => ReferenceCoordinateSequence(xx.map(_._2)))

    val matches: MultiLineString = MonitorRouteAnalysisSupport.toMultiLineString(
      referenceSampleCoordinates,
      okSequences
    )

    val deviations = nokSequences.zipWithIndex.flatMap { case (sequence, sequenceIndex) =>
      val maxDistance = sequence.indexes.map(index => distances(index)).max
      val lineString = MonitorRouteAnalysisSupport.toLineString(referenceSampleCoordinates, sequence)
      val meters: Long = Math.round(toMeters(lineString.getLength))
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
    DeviationAnalysisResult(deviations, matches)
  }

  private def analyzeDistances(tree: STRtree, referenceSampleCoordinates: Seq[Coordinate]): Vector[Double] = {
    log.infoElapsed {
      val distances = referenceSampleCoordinates.toVector.map { coordinate =>
        val point = geomFactory.createPoint(coordinate)
        val lineStrings = findNearLineStrings(tree, coordinate)
        val lineStringDistances = lineStrings.map { lineString =>
          lineString.distance(point)
        }
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
      (s"distances (samples=${referenceSampleCoordinates.size})", distances)
    }
  }

  private def findNearLineStrings(tree: STRtree, coordinate: Coordinate): Seq[LineString] = {
    val envelope = new Envelope(coordinate)
    envelope.expandBy(.04, .02 /* distance in degrees */)
    val nearLineStrings = tree.query(envelope)
    import scala.jdk.CollectionConverters._
    nearLineStrings.asScala.map(_.asInstanceOf[LineString]).toSeq
  }
}
