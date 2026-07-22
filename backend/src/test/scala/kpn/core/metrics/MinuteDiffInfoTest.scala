package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.util.UnitTest

class MinuteDiffInfoTest extends  UnitTest {

  test("minuteDiffInfo") {
    Time.set(Timestamp(2020, 1, 1, 12, 10, 10))
    val minuteDiffInfo = MinuteDiffInfo.from(123, Timestamp(2020, 1, 1, 12, 0, 0))
    minuteDiffInfo.id should equal(123)
    minuteDiffInfo.timestamp should equal(ActionTimestamp(2020, 1, 1, 13, 0, 0, 2020, 1, 3))
    minuteDiffInfo.processed should equal(ActionTimestamp(2020, 1, 1, 13, 10, 10, 2020, 1, 3))
    minuteDiffInfo.delay should equal(10 * 60 + 10)
    Time.clear()
  }
}
