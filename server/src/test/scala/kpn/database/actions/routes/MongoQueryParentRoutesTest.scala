package kpn.database.actions.routes

import kpn.core.doc.ParentRouteData
import kpn.core.test.MongoTest

class MongoQueryParentRoutesTest extends MongoTest {

  test("active route ids") {

    val query = new MongoQueryParentRoutes(database)

    database.baseRoutes.save(newBaseRouteDoc(newRouteSummary(11L, name = "route 11"), subRouteIds = Seq(12L)))
    database.baseRoutes.save(newBaseRouteDoc(newRouteSummary(12L, name = "route 12"), subRouteIds = Seq(13L)))
    database.baseRoutes.save(newBaseRouteDoc(newRouteSummary(13L, name = "route 13")))

    assertEqual(
      query.execute(11L),
      Seq.empty
    )

    assertEqual(
      query.execute(12L),
      Seq(
        ParentRouteData(
          11L,
          "route 11"
        )
      )
    )

    assertEqual(
      query.execute(13L),
      Seq(
        ParentRouteData(
          12L,
          "route 12"
        )
      )
    )
  }
}
