package kpn.database.actions.subsets

import kpn.api.common.Country
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.Day
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.doc.NodeDoc
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.core.test.TestObjects.newOrphanNodeInfo

class MongoQuerySubsetOrphanNodesTest extends MongoTest {

  test("orphan node") {
    database.nodes.save(createNodeDoc())
    assertEqual(
      new MongoQuerySubsetOrphanNodes(database).execute(Subset.beCycling),
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

  test("do not include nodes in another country") {
    database.nodes.save(createNodeDoc(country = Country.nl))
    new MongoQuerySubsetOrphanRoutes(database).execute(Subset.beCycling) should equal(Seq.empty)
  }

  test("do not include nodes with a different routeType") {
    database.nodes.save(createNodeDoc(routeType = RouteType.hiking))
    new MongoQuerySubsetOrphanRoutes(database).execute(Subset.beCycling) should equal(Seq.empty)
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
