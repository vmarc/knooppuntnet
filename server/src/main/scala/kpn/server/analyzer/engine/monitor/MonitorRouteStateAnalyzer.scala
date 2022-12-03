package kpn.server.analyzer.engine.monitor

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonReader

case class AnalysisResult(
  deviations: Seq[MonitorRouteDeviation],
  matches: MultiLineString
)

class MonitorRouteStateAnalyzer() {

  private val geomFactory = new GeometryFactory
  private val sampleDistanceMeters = 10
  private val toleranceMeters = 10
  private val log = Log(classOf[MonitorRouteStateAnalyzer])

  def analyze(
    route: MonitorRoute,
    routeReference: MonitorRouteReference,
    routeRootRelation: Relation,
    now: Timestamp
  ): MonitorRouteState = {

    val routeSegments = MonitorRouteAnalysisSupport.toRouteSegments(routeRootRelation)
    val routeAnalysis = analyzeChange(routeReference, routeRootRelation, routeSegments)

    //    val subRelations = RelationUtil.relationsInRelation(routeRootRelation)
    //    val subRelationDatas = subRelations.map { subRouteRelation =>
    //      val subRouteSegments = MonitorRouteAnalysisSupport.toRouteSegments(subRouteRelation)
    //      MonitorSubRelationData(subRouteRelation, subRouteSegments)
    //    }

    val happy = routeAnalysis.gpxDistance > 0 &&
      routeAnalysis.deviations.isEmpty &&
      routeAnalysis.osmSegments.size == 1

    MonitorRouteState(
      ObjectId(),
      route._id,
      now,
      routeAnalysis.wayCount,
      routeAnalysis.osmDistance,
      routeAnalysis.gpxDistance,
      routeAnalysis.bounds,
      Some(routeReference._id),
      routeAnalysis.osmSegments,
      routeAnalysis.matchesGeometry,
      routeAnalysis.deviations,
      happy
    )
  }

  private def analyzeChange(
    reference: MonitorRouteReference,
    routeRelation: Relation,
    osmRouteSegments: Seq[MonitorRouteSegmentData]
  ): MonitorRouteAnalysis = {

    val referenceGeoJson = new GeoJsonReader().read(reference.geometry)
    val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(referenceGeoJson)

    val analysisResults = referenceLineStrings.map { referenceLineString =>
      val referenceMeters = MonitorRouteAnalysisSupport.toMeters(referenceLineString.getLength)
      val distanceBetweenSamples = sampleDistanceMeters.toDouble * referenceLineString.getLength / referenceMeters
      val densifiedReference = Densifier.densify(referenceLineString, distanceBetweenSamples)
      val referenceSampleCoordinates = densifiedReference.getCoordinates.toSeq

      val distances = log.infoElapsed {
        val result = referenceSampleCoordinates.toList.map { coordinate =>
          val point = geomFactory.createPoint(coordinate)
          val lineStringDistances = osmRouteSegments.map(segment => segment.lineString.distance(point))
          if (lineStringDistances.nonEmpty) {
            toMeters(lineStringDistances.min)
          }
          else {
            0
          }
        }
        (s"distances (refPoints=${referenceSampleCoordinates.size}, osmSegments=${osmRouteSegments.size})", result)
      }

      val withinTolerance = distances.map(distance => distance < toleranceMeters)
      val okAndIndexes = withinTolerance.zipWithIndex.map { case (ok, index) => ok -> index }
      val splittedOkAndIndexes = MonitorRouteAnalysisSupport.split(okAndIndexes)

      val matches: MultiLineString = MonitorRouteAnalysisSupport.toMultiLineString(
        referenceSampleCoordinates,
        splittedOkAndIndexes.filter(_.head._1)
      )

      val noks = splittedOkAndIndexes.filterNot(_.head._1)

      val nok = log.infoElapsed {
        val result = noks.zipWithIndex.flatMap { case (segment, segmentIndex) =>
          val segmentIndexes = segment.map(_._2).toSet
          val maxDistance = distances.zipWithIndex.filter { case (distance, index) =>
            segmentIndexes.contains(index)
          }.map { case (distance, index) =>
            distance
          }.max

          val lineString = MonitorRouteAnalysisSupport.toLineString(referenceSampleCoordinates, segment)
          val meters: Long = Math.round(toMeters(lineString.getLength))

          if (meters == 0L) {
            None
          }
          else {
            val bounds = MonitorRouteAnalysisSupport.toBounds(lineString.getCoordinates.toSeq)
            val geoJson = MonitorRouteAnalysisSupport.toGeoJson(lineString)
            Some(
              MonitorRouteDeviation(
                segmentIndex + 1,
                meters,
                maxDistance.toLong,
                bounds,
                geoJson
              )
            )
          }
        }
        (s"noks", result)
      }

      AnalysisResult(nok, matches)
    }

    val noks = analysisResults.flatMap(_.deviations)

    val deviations: Seq[MonitorRouteDeviation] = noks.sortBy(_.distance).reverse.zipWithIndex.map { case (s, index) =>
      s.copy(id = index + 1)
    }

    val allMatches = geomFactory.createGeometryCollection(analysisResults.map(_.matches).toArray)

    val gpxDistance = Math.round(toMeters(referenceLineStrings.map(_.getLength).sum / 1000))
    val osmDistance = Math.round(osmRouteSegments.map(_.segment.meters).sum.toDouble / 1000)

    val gpxGeometry = MonitorRouteAnalysisSupport.toGeoJson(referenceGeoJson)
    val matchesGeometry = Some(MonitorRouteAnalysisSupport.toGeoJson(allMatches))

    // TODO merge gpx bounds + ok
    val bounds = Util.mergeBounds(osmRouteSegments.map(_.segment.bounds) ++ deviations.map(_.bounds))

    val wayCount = {
      val allRelations = Seq(routeRelation) ++ routeRelation.relationMembers.map(_.relation)
      val allWayMembers = allRelations.flatMap(relation => relation.wayMembers)
      allWayMembers.size
    }

    MonitorRouteAnalysis(
      routeRelation,
      wayCount,
      osmDistance,
      gpxDistance,
      bounds,
      osmRouteSegments.map(_.segment),
      Some(gpxGeometry),
      matchesGeometry,
      deviations
    )
  }
}
