package kpn.database.actions.routes

import kpn.api.common.SharedTestObjects
import kpn.database.base.Database
import kpn.core.doc.Label
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteNodeIdsTest extends UnitTest with SharedTestObjects {

  test("collect ids of nodes referenced in multiple routes") {
    withDatabase { database =>

      buildRoute(database, 11L, Seq(1001L, 1002L))
      buildRoute(database, 12L, Seq(1002L, 1003L))
      buildRoute(database, 13L, Seq(1004L, 1001L))

      val query = new MongoQueryRouteNodeIds(database)

      query.execute(Seq(11L)) should equal(Seq(1001L, 1002L))
      query.execute(Seq(11L, 12L, 13L)) should equal(Seq(1001L, 1002L, 1003L, 1004L))
      query.execute(Seq(14L)) should equal(Seq.empty)
    }
  }

  private def buildRoute(database: Database, routeId: Long, nodeRefs: Seq[Long]): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(routeId),
        labels = Seq(Label.active),
        nodeRefs = nodeRefs
      )
    )
  }
}
