package kpn.database.actions.nodes

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNodeDoc

import scala.language.postfixOps

class MongoQueryKnownNodeIdsTest extends MongoTest {

  test("execute") {
    val query = new MongoQueryKnownNodeIds(database)

    database.nodes.save(newNodeDoc(1001))
    database.nodes.save(newNodeDoc(1002))
    database.nodes.save(newNodeDoc(1003))

    query.execute(Seq(1001L, 1002L, 1004L)) should equal(Seq(1001L, 1002L))
  }
}
