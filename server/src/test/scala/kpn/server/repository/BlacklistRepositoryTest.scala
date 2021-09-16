package kpn.server.repository

import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.analyzer.engine.changes.data.BlacklistEntry

class BlacklistRepositoryTest extends UnitTest {

  test("blacklist") {
    withDatabase { database =>

      val blacklistRepository = new BlacklistRepositoryImpl(database)

      val emptyBlacklist = blacklistRepository.get()
      emptyBlacklist.networks shouldBe empty
      emptyBlacklist.routes shouldBe empty
      emptyBlacklist.nodes shouldBe empty

      val blacklist = Blacklist(
        Seq(
          BlacklistEntry(1, "network1", "reason1"),
          BlacklistEntry(2, "network2", "reason2")
        ),
        Seq(
          BlacklistEntry(11, "route1", "reason11"),
          BlacklistEntry(12, "route2", "reason12")
        ),
        Seq(
          BlacklistEntry(101, "node1", "reason101"),
          BlacklistEntry(102, "node2", "reason102")
        )
      )

      blacklistRepository.save(blacklist)

      blacklistRepository.get() should equal(blacklist)
    }
  }

  test("blacklist is cached for 30 seconds") {
    withDatabase { database =>

      val blacklistRepository = new BlacklistRepositoryImpl(database)

      val blacklist1 = Blacklist(Seq(BlacklistEntry(1, "", "")))
      val blacklist2 = Blacklist(Seq(BlacklistEntry(2, "", "")))

      blacklistRepository.save(blacklist1, 1)
      database.blacklists.save(blacklist2)

      blacklistRepository.get(2000) should equal(blacklist1)
      blacklistRepository.get(31000) should equal(blacklist2)
    }
  }
}
