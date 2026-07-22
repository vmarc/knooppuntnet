package kpn.server.analyzer.engine.changes.route.base

/**
 * Analyzes changes in route geometries by comparing way IDs and coordinates.
 * Identifies ways that have been added, removed, updated or remained unchanged.
 */
object RouteGeometryWayAnalyzer {

  def analyze(
    beforeWays: Seq[WayCoordinates],
    afterWays: Seq[WayCoordinates]
  ): GeometryDiffWayIds = {

    val beforeWayIds = beforeWays.map(_.wayId).toSet
    val afterWayIds = afterWays.map(_.wayId).toSet

    val common = beforeWayIds intersect afterWayIds
    val added = afterWayIds -- beforeWayIds
    val removed = beforeWayIds -- afterWayIds

    val updated = findUpdated(beforeWays, afterWays, common)
    val unchanged = common -- updated

    GeometryDiffWayIds(
      unchanged,
      added,
      removed,
      updated
    )
  }

  private def findUpdated(
    beforeWays: Seq[WayCoordinates],
    afterWays: Seq[WayCoordinates],
    common: Set[Long]
  ): Set[Long] = {

    // Create maps for faster lookup when checking for updates
    val beforeWayMap = beforeWays.map(way => way.wayId -> way.coordinates).toMap
    val afterWayMap = afterWays.map(way => way.wayId -> way.coordinates).toMap

    common.filter { wayId =>
      val beforeCoords = beforeWayMap.get(wayId)
      val afterCoords = afterWayMap.get(wayId)
      beforeCoords != afterCoords
    }
  }
}
