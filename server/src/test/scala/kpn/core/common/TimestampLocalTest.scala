package kpn.core.common

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest

class TimestampLocalTest extends UnitTest with SharedTestObjects {

  private val rm = scala.reflect.runtime.currentMirror

  test("test node object") {
    val node = newNode(1001, timestamp = Timestamp(2018, 8, 11, 0, 0, 0))
    TimestampLocal.localize(node)
    node.timestamp should equal(Timestamp(2018, 8, 11, 2, 0, 0))
  }

  test("test collection") {
    val list = List(Timestamp(2018, 8, 11, 0, 0, 0), Timestamp(2018, 8, 11, 12, 0, 0))
    TimestampLocal.localize(list)
    list should equal(List(Timestamp(2018, 8, 11, 2, 0, 0), Timestamp(2018, 8, 11, 14, 0, 0)))
  }

}
