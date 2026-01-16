package kpn.database.actions.routes

import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Day
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.Timestamps

class MongoQueryOrphanRoutesTest extends MongoTest {

  test("investigate") {

    val query = new MongoQueryOrphanRoutes(database)

    database.routes.save(
      newRouteDoc(
        11L,
        labels = Seq("broken"),
        base = newRouteBaseData(
          name = "route 11",
          meters = 1011,
          lastSurvey = Some(Day(2015, 8, 11)),
          lastUpdated = Timestamps.default,
        )
      )
    )

    database.routes.save(
      newRouteDoc(
        12L,
        base = newRouteBaseData(
          name = "route 12",
          meters = 1012,
          lastSurvey = Some(Day(2015, 8, 12)),
          lastUpdated = Timestamps.default,
        )
      )
    )

    assertEqual(
      query.execute(),
      Seq(
        OrphanRouteInfo(
          id = 11L,
          name = "route 11",
          meters = 1011,
          lastSurvey = Some("2015-08-11"),
          lastUpdated = Timestamps.default,
          facts = Seq.empty,
          investigate = true
        ),
        OrphanRouteInfo(
          id = 12L,
          name = "route 12",
          meters = 1012,
          lastSurvey = Some("2015-08-12"),
          lastUpdated = Timestamps.default,
          facts = Seq.empty,
          investigate = false
        )
      )
    )
  }
}
