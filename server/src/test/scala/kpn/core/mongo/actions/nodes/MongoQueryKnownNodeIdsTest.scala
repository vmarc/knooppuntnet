package kpn.core.mongo.actions.nodes

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryKnownNodeIdsTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryKnownNodeIds(database)

      database.nodes.save(newNodeInfo(1001))
      database.nodes.save(newNodeInfo(1002))
      database.nodes.save(newNodeInfo(1003))

      query.execute(Seq(1001L, 1002L, 1004L)) should equal(Seq(1001L, 1002L))
    }
  }
}
