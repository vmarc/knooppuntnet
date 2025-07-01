package kpn.database.actions.routes

import kpn.api.common.FeatureLayer
import kpn.api.common.RouteType
import kpn.core.test.MongoTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId

class MongoQueryRouteTileIdsTest extends MongoTest {

  test("execute") {
    val query = new MongoQueryRouteTileIds(database)

    database.routeTiles.save(buildTile(1, Seq(RouteType.hiking), 2))
    database.routeTiles.save(buildTile(2, Seq(RouteType.hiking), 2))
    database.routeTiles.save(buildTile(3, Seq(RouteType.hiking), 2))
    database.routeTiles.save(buildTile(4, Seq(RouteType.hiking), 1))
    database.routeTiles.save(buildTile(5, Seq(RouteType.hiking, RouteType.cycling), 1))

    query.execute(RouteType.hiking) should equal(
      Seq(
        TileId(1, 1, 1),
        TileId(1, 1, 2),
      )
    )

    query.execute(RouteType.cycling) should equal(
      Seq(
        TileId(1, 1, 1),
      )
    )
  }

  private def buildTile(id: Long, routeTypes: Seq[RouteType], y: Long): RouteTileInfo = {
    RouteTileInfo(
      _id = id.toString,
      routeId = id,
      routeName = id.toString,
      routeTypes = routeTypes,
      z = 1,
      x = 1,
      y = y,
      layer = FeatureLayer.route,
      scope = None,
      survey = None,
      error = None,
      proposed = false,
      segments = Seq.empty
    )
  }
}
