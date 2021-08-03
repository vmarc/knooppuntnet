package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorGroup
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MonitorAdminGroupRepositoryTest extends UnitTest with SharedTestObjects {

  test("all/add/delete") {
    withDatabase { database =>
      val repository = new MonitorAdminGroupRepositoryImpl(database)

      repository.groups() shouldBe empty
      repository.group("name1") should equal(None)

      repository.saveGroup(MonitorGroup("name1", "description1"))
      repository.saveGroup(MonitorGroup("name2", "description2"))
      repository.group("name1") should equal(Some(MonitorGroup("name1", "description1")))
      repository.group("name2") should equal(Some(MonitorGroup("name2", "description2")))
      repository.groups() should matchTo(
        Seq(
          MonitorGroup("name1", "description1"),
          MonitorGroup("name2", "description2")
        )
      )

      repository.deleteGroup("name1")
      repository.groups() should matchTo(
        Seq(
          MonitorGroup("name2", "description2")
        )
      )

      repository.deleteGroup("name2")
      repository.groups() shouldBe empty
    }
  }
}
