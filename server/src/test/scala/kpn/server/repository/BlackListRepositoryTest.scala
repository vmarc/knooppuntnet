package kpn.server.repository

import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.changes.data.BlackList
import kpn.server.analyzer.engine.changes.data.BlackListEntry
import org.scalatest.FunSuite
import org.scalatest.Matchers

class BlackListRepositoryTest extends FunSuite with Matchers {

  test("blacklist") {
    withDatabase { database =>

      val blackList = BlackList(
        Seq(
          BlackListEntry(1, "network1", "reason1"),
          BlackListEntry(2, "network2", "reason2")
        ),
        Seq(
          BlackListEntry(11, "route1", "reason11"),
          BlackListEntry(12, "route2", "reason12")
        ),
        Seq(
          BlackListEntry(101, "node1", "reason101"),
          BlackListEntry(102, "node2", "reason102")
        )
      )

      val blackListRepository: BlackListRepository = new BlackListRepositoryImpl(database)

      blackListRepository.save(blackList)

      blackListRepository.get should equal(blackList)
    }
  }
}
