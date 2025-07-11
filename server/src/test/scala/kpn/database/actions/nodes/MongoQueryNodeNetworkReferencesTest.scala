package kpn.database.actions.nodes

import kpn.api.common.RouteScope
import kpn.api.common.RouteScope.regional
import kpn.api.common.RouteType
import kpn.api.common.RouteType.hiking
import kpn.api.common.common.Reference
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkInfoNodeDetail
import kpn.core.test.TestObjects.newNetworkSummary

class MongoQueryNodeNetworkReferencesTest extends MongoTest {

  test("execute") {

    database.networks.save(
      newNetworkDoc(
        1L,
        summary = newNetworkSummary(
          name = "network-1",
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
        ),
        nodes = Seq(
          newNetworkInfoNodeDetail(1001),
          newNetworkInfoNodeDetail(1002),
        )
      )
    )
    database.networks.save(
      newNetworkDoc(
        2L,
        summary = newNetworkSummary(
          name = "network-2",
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
        ),
        nodes = Seq(
          newNetworkInfoNodeDetail(1001),
          newNetworkInfoNodeDetail(1002),
          newNetworkInfoNodeDetail(1003),
        )
      )
    )

    val query = new MongoQueryNodeNetworkReferences(database)
    query.execute(1001L) should equal(
      Seq(
        Reference(hiking, regional, 1L, "network-1", None),
        Reference(hiking, regional, 2L, "network-2", None)
      )
    )
  }

  test("non-active networks are not included") {

    database.networks.save(
      newNetworkDoc(
        1L,
        summary = newNetworkSummary(
          name = "network-1",
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
        ),
        nodes = Seq(
          newNetworkInfoNodeDetail(1001)
        )
      )
    )
    database.networks.save(
      newNetworkDoc(
        2L,
        active = false,
        summary = newNetworkSummary(
          name = "network-2",
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
        ),
        nodes = Seq(
          newNetworkInfoNodeDetail(1001)
        )
      )
    )

    val query = new MongoQueryNodeNetworkReferences(database)
    query.execute(1001L) should equal(
      Seq(
        Reference(hiking, regional, 1L, "network-1", None)
      )
    )
  }
}
