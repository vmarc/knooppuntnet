package kpn.database.actions.nodes

import kpn.api.common.Country
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.doc.NodeDoc
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.core.test.TestObjects.newOrphanNodeInfo

class MongoQueryOrphanNodesTest extends MongoTest {

  test("orphan node") {
    database.nodes.save(createNodeDoc())
    assertEqual(
      new MongoQueryOrphanNodes(database).execute(),
      Seq(
        newOrphanNodeInfo(
          1001,
          name = "01-02",
          longName = Some("long-name"),
          lastUpdated = Timestamp(2020, 8, 11),
          proposed = true
        )
      )
    )
  }

  private def createNodeDoc(
    country: Country = Country.be,
    routeType: RouteType = RouteType.cycling,
    lastSurvey: Option[Day] = None
  ): NodeDoc = {
    newNodeDoc(
      1001L,
      base = newNodeBaseData(
        name = Some("01-02"),
        names = Seq(
          newNodeName(
            routeType = RouteType.cycling,
            routeScope = RouteScope.local,
            name = "01-02",
            longName = Some("long-name"),
            proposed = true
          )
        ),
        lastUpdated = Timestamp(2020, 8, 11),
        country = Some(country)
      ),
      labels = Seq(
        Label.country(country),
        Label.routeType(routeType)
      ),
    )
  }
}
