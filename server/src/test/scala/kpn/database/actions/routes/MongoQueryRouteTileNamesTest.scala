package kpn.database.actions.routes

import kpn.api.common.RouteType
import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.tiles.domain.TileId

class MongoQueryRouteTileNamesTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryRouteTileNames(database)

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
  }

  private def buildTile(id: Long, routeTypes: Seq[RouteType], y: Long): RouteTileDoc = {
    RouteTileDoc(
      _id = id.toString,
      routeId = id,
      routeName = id.toString,
      routeTypes = routeTypes,
      z = 1,
      x = 1,
      y = y,
      layer = "",
      scope = None,
      survey = None,
      error = None,
      segments = Seq.empty
    )
  }
}
