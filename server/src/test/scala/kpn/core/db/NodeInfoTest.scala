package kpn.core.db

import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeInfoTest extends FunSuite with Matchers with SharedTestObjects {

  test("name") {
    newNodeInfo(1, tags = Tags.from("rwn_ref" -> "01")).name should equal("01")
    newNodeInfo(1, tags = Tags.from("rcn_ref" -> "01")).name should equal("01")
    newNodeInfo(1, tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")).name should equal("01 / 02")
  }
}
