package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.Bounds
import kpn.api.common.LatLon
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.WayInfo
import kpn.api.common.route.GeometryDiffAnalyzer
import kpn.api.common.route.RouteChangeInfo
import kpn.api.common.route.WayGeometry

class RouteChangeInfoBuilder {

  def build(routeChange: RouteChange, changeSetInfos: Seq[ChangeSetInfo]): RouteChangeInfo = {

    val changeSetInfo = changeSetInfos.find(changeSetInfo => changeSetInfo.id == routeChange.key.changeSetId)

    if (routeChange.before.isDefined && routeChange.after.isDefined) {

      val before = routeChange.before.get
      val after = routeChange.after.get

      val waysBefore = WayGeometry.from(before)
      val waysAfter = WayGeometry.from(after)
      val geometryDiff = new GeometryDiffAnalyzer().analysis(waysBefore, waysAfter)
      val comment = changeSetInfo.flatMap(_.tags("comment"))

      val bounds = {
        val latLons = geometryDiff match {
          case Some(diff) =>
            // note that the 'common' points are not taken into account here, so that we zoom in on the actual changes
            val segments = diff.before ++ diff.after
            if (segments.nonEmpty) {
              segments.flatMap(s => Seq(s.p1, s.p2))
            }
            else {
              val allSegments = diff.before ++ diff.after ++ diff.common
              allSegments.flatMap(s => Seq(s.p1, s.p2))
            }
          case None =>
            Seq.empty
        }
        Bounds.from(latLons)
      }

      RouteChangeInfo(
        after.id,
        after.relation.version,
        routeChange.key,
        comment,
        routeChange.before.map(_.relation.toMeta),
        routeChange.after.map(_.relation.toMeta),
        routeChange.removedWays.map(w => WayInfo(w.id, w.version, w.changeSetId, w.timestamp, w.tags)),
        routeChange.addedWays.map(w => WayInfo(w.id, w.version, w.changeSetId, w.timestamp, w.tags)),
        routeChange.updatedWays,
        routeChange.diffs,
        after.networkNodes,
        changeSetInfo,
        geometryDiff = geometryDiff,
        bounds = bounds,
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else if (routeChange.before.isDefined) {

      val routeData = routeChange.before.get
      val ways = WayGeometry.from(routeData)
      val geometryDiff = new GeometryDiffAnalyzer().analysis(Seq.empty, ways)
      val comment = changeSetInfo.flatMap(_.tags("comment"))

      val bounds = {
        val nodeLatLons: Seq[LatLon] = routeData.networkNodes
        val wayLatLons: Seq[LatLon] = ways.flatMap(_.nodes)
        val latLons = (nodeLatLons ++ wayLatLons).distinct
        Bounds.from(latLons)
      }

      RouteChangeInfo(
        routeData.id,
        routeData.relation.version,
        routeChange.key,
        comment,
        routeChange.before.map(_.relation.toMeta),
        routeChange.after.map(_.relation.toMeta),

        routeChange.removedWays.map(w => WayInfo(w.id, w.version, w.changeSetId, w.timestamp, w.tags)),
        routeChange.addedWays.map(w => WayInfo(w.id, w.version, w.changeSetId, w.timestamp, w.tags)),
        routeChange.updatedWays,

        routeChange.diffs,
        routeData.networkNodes,
        changeSetInfo,
        geometryDiff = geometryDiff,
        bounds = bounds,
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else if (routeChange.after.isDefined) {

      val routeData = routeChange.after.get
      val ways = WayGeometry.from(routeData)
      val geometryDiff = new GeometryDiffAnalyzer().analysis(Seq.empty, ways)
      val comment = changeSetInfo.flatMap(_.tags("comment"))

      val bounds = {
        val nodeLatLons: Seq[LatLon] = routeData.nodes
        val wayLatLons: Seq[LatLon] = ways.flatMap(_.nodes)
        val latLons = (nodeLatLons ++ wayLatLons).distinct
        Bounds.from(latLons)
      }

      RouteChangeInfo(
        routeData.id,
        routeData.relation.version,
        routeChange.key,
        comment,
        routeChange.before.map(_.relation.toMeta),
        routeChange.after.map(_.relation.toMeta),
        routeChange.removedWays.map(w => WayInfo(w.id, w.version, w.changeSetId, w.timestamp, w.tags)),
        routeChange.addedWays.map(w => WayInfo(w.id, w.version, w.changeSetId, w.timestamp, w.tags)),
        routeChange.updatedWays,
        routeChange.diffs,
        routeData.networkNodes,
        changeSetInfo,
        geometryDiff = geometryDiff,
        bounds = bounds,
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else {
      throw new IllegalStateException("Cannot derive RouteChangeInfo from RouteChange " + routeChange)
    }
  }
}
