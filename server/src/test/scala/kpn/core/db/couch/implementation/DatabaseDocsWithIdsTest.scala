package kpn.core.db.couch.implementation

import kpn.core.TestObjects
import kpn.core.db.NodeDoc
import kpn.core.db.NodeDocViewResult
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseDocsWithIdsTest extends FunSuite with Matchers with TestObjects {

  test("docsWithIds") {
    withDatabase(database => {
      val nodeInfo1 = newNodeInfo(1001)
      val doc1 = NodeDoc("node:1001", nodeInfo1, None)
      database.save(doc1)

      val nodeInfo2 = newNodeInfo(1002)
      val doc2 = NodeDoc("node:1002", nodeInfo2, None)
      database.save(doc2)

      val result: NodeDocViewResult = database.docsWithIds(Seq("node:1001", "node:1002", "node:1003"), classOf[NodeDocViewResult], stale = false)
      result.rows.flatMap(_.doc).map(_.node.id) should equal(Seq(1001, 1002))
    });
  }

  test("docsWithIds - no results") {
    withDatabase(database => {
      val result: NodeDocViewResult = database.docsWithIds(Seq("node:1001", "node:1002"), classOf[NodeDocViewResult], stale = false)
      result.rows.flatMap(_.doc).length should equal(0)
    });
  }

}
