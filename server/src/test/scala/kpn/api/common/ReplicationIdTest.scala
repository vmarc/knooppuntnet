package kpn.api.common

import org.scalatest.FunSuite
import org.scalatest.Matchers

class ReplicationIdTest extends FunSuite with Matchers {

  test("alternative constructors") {
    ReplicationId("001/002/003") should equal(ReplicationId(1, 2, 3))
    ReplicationId(1002003) should equal(ReplicationId(1, 2, 3))
  }

  test("sequenceNumber") {
    ReplicationId(1, 2, 3).number should equal(1002003)
  }

  test("range") {
    ReplicationId.range(ReplicationId(1, 442, 950), ReplicationId(1, 443, 50)).size should equal(101)
  }

  test("name") {
    ReplicationId(1, 2, 3).name should equal("001/002/003")
  }
}
