package kpn.shared

import org.scalatest.FunSuite
import org.scalatest.Matchers

import kpn.shared.network.NetworkChangesPage
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkNodesPage
import kpn.shared.network.NetworkDetailsPage
import kpn.shared.network.NetworkRoutesPage
import kpn.shared.route.RouteInfo
import kpn.shared.subset.SubsetChangesPage
import kpn.shared.subset.SubsetFactsPage
import kpn.shared.subset.SubsetNetworksPage
import kpn.shared.subset.SubsetOrphanNodesPage
import kpn.shared.subset.SubsetOrphanRoutesPage

class BooPickleTest extends FunSuite with Matchers {

  import KpnPicklers._
  import boopickle.DefaultBasic._

  test("Route") {
    val data: Option[RouteInfo] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[RouteInfo]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("NetworkChangesPage") {
    val data: Option[NetworkChangesPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[NetworkChangesPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("SubsetOrphanRoutesPage") {
    val data: Option[SubsetOrphanRoutesPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[SubsetOrphanRoutesPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("SubsetOrphanNodesPage") {
    val data: Option[SubsetOrphanNodesPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[SubsetOrphanNodesPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("SubsetNetworksPage") {
    val data: Option[SubsetNetworksPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[SubsetNetworksPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("SubsetFactsPage") {
    val data: Option[SubsetFactsPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[SubsetFactsPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("SubsetChangesPage") {
    val data: Option[SubsetChangesPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[SubsetChangesPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("NetworkRoutesPage") {
    val data: Option[NetworkRoutesPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[NetworkRoutesPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("NetworkNodesPage") {
    val data: Option[NetworkNodesPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[NetworkNodesPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("NetworkPage") {
    val data: Option[NetworkDetailsPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[NetworkDetailsPage]].fromBytes(buf)
    unpickled should equal(data)
  }

  test("NetworkFactsPage") {
    val data: Option[NetworkFactsPage] = None
    val buf = Pickle.intoBytes(data)
    val unpickled = Unpickle[Option[NetworkFactsPage]].fromBytes(buf)
    unpickled should equal(data)
  }

}
