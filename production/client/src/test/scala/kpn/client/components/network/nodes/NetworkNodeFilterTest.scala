// TODO migrate to Angular
package kpn.client.components.network.nodes

import kpn.client.filter.FilterOption
import kpn.shared.Fact
import kpn.shared.NodeIntegrityCheck
import kpn.shared.TimeInfo
import kpn.shared.Timestamp
import kpn.shared.common.Ref
import kpn.shared.data.Tags
import kpn.shared.network.NetworkNodeInfo2
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkNodeFilterTest extends FunSuite with Matchers {

  test("filter definedInNetworkRelation") {
    val nodes = Seq(
      networkNodeInfo2(1, "01", definedInRelation = true),
      networkNodeInfo2(2, "02")
    )

    filter(NetworkNodeFilterCriteria(), nodes) should equal(Seq(1, 2))
    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(true)), nodes) should equal(Seq(1))
    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(false)), nodes) should equal(Seq(2))
  }

  test("filter definedInRouteRelation") {
    val nodes = Seq(
      networkNodeInfo2(1, "01", definedInRoute = true),
      networkNodeInfo2(2, "02")
    )

    filter(NetworkNodeFilterCriteria(), nodes) should equal(Seq(1, 2))
    filter(NetworkNodeFilterCriteria(definedInRouteRelation = Some(true)), nodes) should equal(Seq(1))
    filter(NetworkNodeFilterCriteria(definedInRouteRelation = Some(false)), nodes) should equal(Seq(2))
  }

  test("filter referencedInRoute") {
    val nodes = Seq(
      networkNodeInfo2(1, "01", definedInRelation = true),
      networkNodeInfo2(2, "02")
    )

    filter(NetworkNodeFilterCriteria(), nodes) should equal(Seq(1, 2))
    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(true)), nodes) should equal(Seq(1))
    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(false)), nodes) should equal(Seq(2))
  }

  test("filter roleConnection") {
    val nodes = Seq(
      networkNodeInfo2(1, "01", roleConnection = true),
      networkNodeInfo2(2, "02")
    )

    filter(NetworkNodeFilterCriteria(), nodes) should equal(Seq(1, 2))
    filter(NetworkNodeFilterCriteria(roleConnection = Some(true)), nodes) should equal(Seq(1))
    filter(NetworkNodeFilterCriteria(roleConnection = Some(false)), nodes) should equal(Seq(2))
  }

  test("filter integrityCheck") {
    pending
    ()
  }

  test("filter integrityCheckFailed") {
    pending
    ()
  }

  test("filter lastUpdated") {
    pending
    ()
  }

  test("filter combination") {

    def updateCriteria(criteria: NetworkNodeFilterCriteria): Unit = {
    }

    val nodes = Seq(
      networkNodeInfo2(1, "01"),
      networkNodeInfo2(2, "02", definedInRoute = true),
      networkNodeInfo2(3, "03", definedInRelation = true),
      networkNodeInfo2(4, "04", definedInRelation = true),
      networkNodeInfo2(5, "05", definedInRelation = true),
      networkNodeInfo2(6, "06", definedInRelation = true, definedInRoute = true)
    )

    filter(NetworkNodeFilterCriteria(), nodes) should equal(Seq(1, 2, 3, 4, 5, 6))

    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(false)), nodes) should equal(Seq(1, 2))
    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(true)), nodes) should equal(Seq(3, 4, 5, 6))

    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(false), definedInRouteRelation = Some(false)), nodes) should equal(Seq(1))
    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(false), definedInRouteRelation = Some(true)), nodes) should equal(Seq(2))
    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(true), definedInRouteRelation = Some(false)), nodes) should equal(Seq(3, 4, 5))
    filter(NetworkNodeFilterCriteria(definedInNetworkRelation = Some(true), definedInRouteRelation = Some(true)), nodes) should equal(Seq(6))

    val criteria = NetworkNodeFilterCriteria(definedInNetworkRelation = Some(true), definedInRouteRelation = Some(false))
    val filterOptions = new NetworkNodeFilter(TimeInfo(), criteria, updateCriteria).filterOptions(nodes)

    // matching both criteria: 03, 04, 05

    {
      val group = filterOptions.groups.head
      group.name should equal("definedInNetworkRelation")
      val options = group.options

      assert(options.head.sameAs(FilterOption("all", 4, selected = false))) // 01, 03, 04, 05
      assert(options(1).sameAs(FilterOption("yes", 3, selected = true))) // 03, 04, 05
      assert(options(2).sameAs(FilterOption("no", 1, selected = false))) // 01
    }

    {
      val group = filterOptions.groups(1)
      group.name should equal("definedInRouteRelation")
      val options = group.options
      assert(options.head.sameAs(FilterOption("all", 4, selected = false))) // 03, 04, 05, 06
      assert(options(1).sameAs(FilterOption("yes", 1, selected = false))) // 06
      assert(options(2).sameAs(FilterOption("no", 3, selected = true))) // 03, 04, 05
    }
  }

  private def filter(criteria: NetworkNodeFilterCriteria, nodes: Seq[NetworkNodeInfo2]): Seq[Long] = {
    def updateCriteria(criteria: NetworkNodeFilterCriteria): Unit = {
    }

    new NetworkNodeFilter(TimeInfo(), criteria, updateCriteria).filter(nodes).map(_.id)
  }

  private def networkNodeInfo2(
    id: Long,
    title: String,
    number: String = "",
    latitude: String = "",
    longitude: String = "",
    connection: Boolean = false,
    roleConnection: Boolean = false,
    definedInRelation: Boolean = false,
    definedInRoute: Boolean = false,
    timestamp: Timestamp = Timestamp(2015, 1, 1),
    routeReferences: Seq[Ref] = Seq.empty,
    nodeIntegrityCheck: Option[NodeIntegrityCheck] = None,
    facts: Seq[Fact] = Seq.empty,
    tags: Tags = Tags.empty
  ): NetworkNodeInfo2 = {
    NetworkNodeInfo2(
      id,
      title,
      number,
      latitude,
      longitude,
      connection,
      roleConnection,
      definedInRelation,
      definedInRoute,
      timestamp,
      routeReferences,
      nodeIntegrityCheck,
      facts,
      tags
    )
  }
}
