package kpn.server.api.analysis.pages.network

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkChangesPage
import kpn.api.common.network.NetworkSummary
import kpn.core.doc.NetworkDoc
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNetworkChangeInfo
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.server.config.RequestContext
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.NetworkInfoRepository

class NetworkChangesPageBuilderTest extends MongoTest {

  test("network not found") {
    build(3, ChangesParameters()) should equal(None)
  }

  test("execute") {

    // setup
    database.networks.save(buildNetworkDoc())
    database.networkChanges.save(buildNetworkChange())

    // execute
    val result = build(2, ChangesParameters())

    // verify
    assertEqual(
      result.get,
      NetworkChangesPage(
        network = NetworkSummary(
          name = Some("network-name"),
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          factCount = 5,
          nodeCount = 10,
          routeCount = 15,
        ),
        filterOptions = Seq(
          ChangesFilterOption(
            level = "all",
            name = "all",
            year = None,
            month = None,
            day = None,
            totalCount = 1,
            impactedCount = 0,
            current = true
          ),
          ChangesFilterOption(
            level = "year",
            name = "2015",
            year = Some(2015),
            month = None,
            day = None,
            totalCount = 1,
            impactedCount = 0,
          )
        ),
        changes = Seq(
          newNetworkChangeInfo(
            key = newChangeKey(elementId = 2),
            networkId = 2
          )
        ),
        totalCount = 1
      )
    )
  }

  private def buildNetworkDoc(): NetworkDoc = {
    newNetworkDoc(
      _id = 2,
      base = newNetworkBaseData(
        name = Some("network-name"),
        routeType = RouteType.cycling,
        routeScope = RouteScope.regional,
      ),
      factCount = 5,
      nodeCount = 10,
      routeCount = 15,
    )
  }

  private def buildNetworkChange(): NetworkChange = {
    newNetworkChange(
      key = newChangeKey(elementId = 2),
    )
  }

  private def build(networkId: Long, parameters: ChangesParameters): Option[NetworkChangesPage] = {
    val changeSetInfoRepository = new ChangeSetInfoRepository(database)
    val networkInfoRepository = new NetworkInfoRepository(database)
    val builder = new NetworkChangesPageBuilder(database, changeSetInfoRepository, networkInfoRepository)
    builder.build(networkId, parameters)
  }

  override def beforeEach(): Unit = {
    super.beforeEach()
    RequestContext.instance.set(
      Some(
        RequestContext(
          "",
          None,
          None,
          None,
          Some("user")
        )
      )
    )
  }

  override def afterEach(): Unit = {
    RequestContext.instance.set(None)
    super.afterEach()
  }
}
