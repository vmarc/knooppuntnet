package kpn.core.mongo.actions.networks

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.MongoNetworkRepositoryImpl

class MongoQueryNetworkTest extends UnitTest with SharedTestObjects {

  test("network not found") {
    withDatabase { database =>
      val result = new MongoQueryNetwork(database).execute(1L)
      result should equal(None)
    }
  }

  test("execute") {
    withDatabase { database =>
      val networkRepository = new MongoNetworkRepositoryImpl(database)
      val networkInfo = newNetworkInfo(
        newNetworkAttributes(1L)
      )
      networkRepository.save(networkInfo)
      val fromDb = networkRepository.networkWithId(1L)
      fromDb should equal(Some(networkInfo))
    }
  }
}
