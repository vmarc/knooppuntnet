package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.api.monitor.domain.MonitorRouteChange

class MonitorRouteRepositoryTest extends UnitTest with SharedTestObjects {

  test("changes/changesCount") {

    withDatabase { database =>

      val routeRepository = new MonitorRouteRepositoryImpl(database)

      val change1 = buildChange("group-1", 101L, 1L, Timestamp(2020, 8, 11), happy = false)
      val change2 = buildChange("group-1", 101L, 2L, Timestamp(2020, 8, 12), happy = true)
      val change3 = buildChange("group-1", 102L, 3L, Timestamp(2020, 8, 13), happy = false)
      val change4 = buildChange("group-2", 103L, 4L, Timestamp(2020, 8, 14), happy = false)
      val change5 = buildChange("group-2", 103L, 5L, Timestamp(2020, 8, 15), happy = true)

      routeRepository.saveRouteChange(change1)
      routeRepository.saveRouteChange(change2)
      routeRepository.saveRouteChange(change3)
      routeRepository.saveRouteChange(change4)
      routeRepository.saveRouteChange(change5)

      routeRepository.changesCount(MonitorChangesParameters()) should equal(5)
      routeRepository.changes(MonitorChangesParameters()) should equal(
        Seq(
          change5,
          change4,
          change3,
          change2,
          change1
        )
      )

      routeRepository.changesCount(MonitorChangesParameters(impact = true)) should equal(2)
      routeRepository.changes(MonitorChangesParameters(impact = true)).shouldMatchTo(
        Seq(
          change5,
          change2
        )
      )

      routeRepository.groupChangesCount("group-1", MonitorChangesParameters()) should equal(3)
      routeRepository.groupChanges("group-1", MonitorChangesParameters()).shouldMatchTo(
        Seq(
          change3,
          change2,
          change1
        )
      )

      routeRepository.groupChangesCount("group-1", MonitorChangesParameters(impact = true)) should equal(1)
      routeRepository.groupChanges("group-1", MonitorChangesParameters(impact = true)).shouldMatchTo(
        Seq(
          change2
        )
      )

      pending // use string monitor id instead of long id

      routeRepository.routeChangesCount("101", MonitorChangesParameters()) should equal(2)
      routeRepository.routeChanges("101", MonitorChangesParameters()).shouldMatchTo(
        Seq(
          change2,
          change1
        )
      )

      routeRepository.routeChangesCount("101", MonitorChangesParameters(impact = true)) should equal(1)
      routeRepository.routeChanges("101", MonitorChangesParameters(impact = true)).shouldMatchTo(
        Seq(
          change2
        )
      )
    }
  }

  private def buildChange(groupName: String /*TODO MON remove*/ , routeId: Long, changeSetId: Long, timestamp: Timestamp, happy: Boolean): MonitorRouteChange = {
    newMonitorRouteChange(
      newChangeKey(
        1,
        timestamp,
        changeSetId,
        routeId
      ),
      happy = happy
    )
  }
}
