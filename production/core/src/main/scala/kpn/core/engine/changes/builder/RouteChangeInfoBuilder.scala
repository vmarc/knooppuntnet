package kpn.core.engine.changes.builder

import kpn.shared.Bounds
import kpn.shared.LatLon
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.changes.details.RouteChange
import kpn.shared.diff.WayInfo
import kpn.shared.route.GeometryDiffAnalyzer
import kpn.shared.route.RouteChangeInfo
import kpn.shared.route.WayGeometry

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
        val nodeLatLons: Seq[LatLon] = before.networkNodes ++ after.networkNodes
        val wayLatLons: Seq[LatLon] = waysBefore.flatMap(_.nodes) ++ waysAfter.flatMap(_.nodes)
        val latLons = (nodeLatLons ++ wayLatLons).distinct
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
        // addedNodes: Seq[Long] = Seq.empty,
        // deletedNodes: Seq[Long] = Seq.empty,
        // commonNodes: Seq[Long] = Seq.empty,
        // addedWayIds: Seq[Long] = Seq.empty,
        // deletedWayIds: Seq[Long] = Seq.empty,
        // commonWayIds: Seq[Long] = Seq.empty,
        geometryDiff = geometryDiff,
        bounds = bounds,
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else if (routeChange.before.isDefined) {

      val routeData = routeChange.before.get
      val ways = WayGeometry.from(routeData)
      val geometryDiff = new GeometryDiffAnalyzer().analysis(Seq(), ways)
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
        routeData.nodes,
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
      val geometryDiff = new GeometryDiffAnalyzer().analysis(Seq(), ways)
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
        routeData.nodes,
        changeSetInfo,
        geometryDiff = geometryDiff,
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else {
      throw new IllegalStateException("Cannot derive RouteChangeInfo from RouteChange " + routeChange)
    }
  }

}
