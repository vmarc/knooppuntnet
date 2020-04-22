package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.app.stats.Figure
import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class OverviewRepositoryTest extends AnyFunSuite with Matchers with SharedTestObjects {

  test("figures") {

    withDatabase { database =>

      val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database)
      networkRepository.save(
        newNetworkInfo(
          newNetworkAttributes(
            1,
            Some(Country.be),
            NetworkType.hiking
          ),
          detail = Some(
            newNetworkInfoDetail(
              nodes = Seq(
                newNetworkInfoNode(101, "01"),
                newNetworkInfoNode(102, "02")
              )
            )
          )
        )
      )

      val repository: OverviewRepository = new OverviewRepositoryImpl(database)

      val figures = repository.figures(stale = false)
      figures("NetworkCount") should equal(Figure("NetworkCount", 1, Map(Subset.beHiking -> 1)))
      figures("NodeCount") should equal(Figure("NodeCount", 2, Map(Subset.beHiking -> 2)))
    }
  }
}
