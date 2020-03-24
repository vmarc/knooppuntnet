package kpn.api.common.status

import kpn.api.custom.Timestamp
import kpn.core.common.Time
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActionTimestampTest extends FunSuite with Matchers {

  test("week numbers") {
    calculateWeek(Timestamp(2019, 12, 30, 12, 0, 0)) should equal(2020, 1, 1)
    calculateWeek(Timestamp(2020, 1, 1, 12, 0, 0)) should equal(2020, 1, 3)
    calculateWeek(Timestamp(2020, 1, 6, 12, 0, 0)) should equal(2020, 2, 1)
    calculateWeek(Timestamp(2020, 12, 27, 12, 0, 0)) should equal(2020, 52, 7)
    calculateWeek(Timestamp(2020, 12, 28, 12, 0, 0)) should equal(2020, 53, 1)
    calculateWeek(Timestamp(2021, 1, 1, 12, 0, 0)) should equal(2020, 53, 5)
    calculateWeek(Timestamp(2021, 1, 2, 12, 0, 0)) should equal(2020, 53, 6)
    calculateWeek(Timestamp(2021, 1, 3, 12, 0, 0)) should equal(2020, 53, 7)
    calculateWeek(Timestamp(2021, 1, 4, 12, 0, 0)) should equal(2021, 1, 1)
  }

  test("minuteDiffInfo") {
    Time.set(Timestamp(2020, 1, 1, 12, 10, 10))
    val minuteDiffInfo = ActionTimestamp.minuteDiffInfo(123, Timestamp(2020, 1, 1, 12, 0, 0))
    minuteDiffInfo.id should equal(123)
    minuteDiffInfo.timestamp should equal(ActionTimestamp(2020, 1, 1, 13, 0, 0, 2020, 1, 3))
    minuteDiffInfo.processed should equal(ActionTimestamp(2020, 1, 1, 13, 10, 10, 2020, 1, 3))
    minuteDiffInfo.delay should equal(10 * 60 + 10)
    Time.clear()
  }

  test("toId") {
    ActionTimestamp(2020, 1, 2, 3, 4, 5, 0, 0, 0).toId should equal("2020-01-02-03-04-05")
  }

  private def calculateWeek(timestamp: Timestamp): (Long, Long, Long) = {
    val actionTimestamp = ActionTimestamp.from(timestamp)
    (actionTimestamp.weekYear, actionTimestamp.weekWeek, actionTimestamp.weekDay)
  }

}
