package kpn.server.repository

import kpn.core.app.stats.Figure
import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OverviewRepositoryTest extends FunSuite with Matchers with SharedTestObjects {

  ignore("figures") {

    withDatabase { database =>

      val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database)
      networkRepository.save(
        newNetwork(
          1,
          Some(Country.be),
          NetworkType.hiking,
          nodes = Seq(
            newNetworkNodeInfo2(101, "01"),
            newNetworkNodeInfo2(102, "02")
          )
        )
      )

      val repository: OverviewRepository = new OverviewRepositoryImpl(database)

      val figures = repository.figures(Couch.uiTimeout)
      figures("NetworkCount") should equal(Figure("NetworkCount", beRwn = 1))
      figures("NodeCount") should equal(Figure("NodeCount", beRwn = 2))
    }
  }
}
