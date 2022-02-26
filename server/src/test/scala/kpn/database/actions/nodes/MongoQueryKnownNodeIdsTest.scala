package kpn.database.actions.nodes

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

import scala.language.postfixOps

class MongoQueryKnownNodeIdsTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryKnownNodeIds(database)

      database.nodes.save(newNodeDoc(1001))
      database.nodes.save(newNodeDoc(1002))
      database.nodes.save(newNodeDoc(1003))

      query.execute(Seq(1001L, 1002L, 1004L)) should equal(Seq(1001L, 1002L))
    }
  }
}
