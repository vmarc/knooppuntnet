package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.app.stats.Figure
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class OverviewRepositoryTest extends UnitTest with SharedTestObjects {

  test("figures") {

    withCouchDatabase { database =>

      val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database, false, null)
      networkRepository.save(
        newNetworkInfo(
          newNetworkAttributes(
            1,
            Some(Country.be),
            NetworkType.hiking,
            nodeCount = 2
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
