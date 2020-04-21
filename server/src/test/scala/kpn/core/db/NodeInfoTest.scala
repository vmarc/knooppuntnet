package kpn.core.db

import kpn.api.custom.Tags
import kpn.api.common.SharedTestObjects
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class NodeInfoTest extends AnyFunSuite with Matchers with SharedTestObjects {

  test("name") {
    newNodeInfo(1, tags = Tags.from("rwn_ref" -> "01")).name should equal("01")
    newNodeInfo(1, tags = Tags.from("rcn_ref" -> "01")).name should equal("01")
    newNodeInfo(1, tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")).name should equal("01 / 02")
  }
}
