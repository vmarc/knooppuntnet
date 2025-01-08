package kpn.server.analyzer.engine.analysis.post

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkScope
import kpn.api.common.RouteType
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.core.doc.OrphanNodeDoc
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.MockLog
import kpn.core.util.UnitTest

class OrphanNodeUpdater_UpdateTest extends UnitTest with SharedTestObjects {

  test("no orphan nodes") {
    withDatabase { database =>
      // 2 nodes in the database
      database.nodes.save(newNodeDoc(1001, country = Some(Country.nl), names = Seq(newNodeName())))
      database.nodes.save(newNodeDoc(1002, country = Some(Country.nl), names = Seq(newNodeName())))
      // initial orphan nodes contents that will be wiped out
      database.orphanNodes.save(newOrphanNodeDoc(Country.nl, RouteType.hiking, 1001))
      // no orphan nodes
      new OrphanNodeUpdater_Update(database, new MockLog()).execute(Seq.empty)
      database.orphanNodes.findAll() should equal(Seq.empty)
    }
  }

  test("orphan node") {
    withDatabase { database =>
      // 2 nodes in the database
      database.nodes.save(newNodeDoc(1001, country = Some(Country.nl), names = Seq(newNodeName())))
      database.nodes.save(
        newNodeDoc(
          1002,
          country = Some(Country.nl),
          names = Seq(
            newNodeName(
              routeType = RouteType.hiking,
              networkScope = NetworkScope.regional,
              name = "01",
              longName = Some("one")
            ),
            newNodeName(
              routeType = RouteType.cycling,
              networkScope = NetworkScope.regional,
              name = "02",
              longName = None,
              proposed = true
            )
          ),
          lastSurvey = Some(Day(2020, 8, None)),
          facts = Seq(Fact.IntegrityCheckFailed)
        )
      )
      // initial orphan nodes contents that will be wiped out
      database.orphanNodes.save(newOrphanNodeDoc(Country.nl, RouteType.hiking, 1002))
      // single orphan node
      new OrphanNodeUpdater_Update(database, new MockLog()).execute(Seq(1002))
      assertEqual(
        database.orphanNodes.findAll().sortBy(_._id),
        Seq(
          OrphanNodeDoc(
            _id = "nl:cycling:1002",
            country = Country.nl,
            routeType = RouteType.cycling,
            nodeId = 1002,
            name = "02",
            longName = None,
            proposed = true,
            lastUpdated = defaultTimestamp,
            lastSurvey = Some(Day(2020, 8, None)),
            facts = Seq(Fact.IntegrityCheckFailed)
          ),
          OrphanNodeDoc(
            _id = "nl:hiking:1002",
            country = Country.nl,
            routeType = RouteType.hiking,
            nodeId = 1002,
            name = "01",
            longName = Some("one"),
            proposed = false,
            lastUpdated = defaultTimestamp,
            lastSurvey = Some(Day(2020, 8, None)),
            facts = Seq(Fact.IntegrityCheckFailed)
          )
        )
      )
    }
  }
}
