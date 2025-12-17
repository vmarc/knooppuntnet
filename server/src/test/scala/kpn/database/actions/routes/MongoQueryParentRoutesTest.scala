package kpn.database.actions.routes

import kpn.core.doc.ParentRouteData
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteSummary

class MongoQueryParentRoutesTest extends MongoTest {

  test("active route ids") {

    val query = new MongoQueryParentRoutes(database)

    database.baseRoutes.save(newBaseRouteDoc(11L, base = newRouteBaseData(summary = newRouteSummary(name = "route 11")), subRouteIds = Seq(12L)))
    database.baseRoutes.save(newBaseRouteDoc(12L, base = newRouteBaseData(summary = newRouteSummary(name = "route 12")), subRouteIds = Seq(13L)))
    database.baseRoutes.save(newBaseRouteDoc(13L, base = newRouteBaseData(summary = newRouteSummary(name = "route 13"))))

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
