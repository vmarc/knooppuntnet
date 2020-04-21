package kpn.core.database.implementation

import kpn.core.TestObjects
import kpn.core.database.doc.NodeDoc
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class DatabaseDeleteDocsWithIdsTest extends FunSuite with Matchers with TestObjects {

  test("deleteDocsWithIds") {
    withDatabase(database => {
      val nodeInfo1 = newNodeInfo(1001)
      val doc1 = NodeDoc("node:1001", nodeInfo1, None)
      database.save(doc1)

      val nodeInfo2 = newNodeInfo(1002)
      val doc2 = NodeDoc("node:1002", nodeInfo2, None)
      database.save(doc2)

      val nodeInfo3 = newNodeInfo(1003)
      val doc3 = NodeDoc("node:1003", nodeInfo3, None)
      database.save(doc3)

      database.deleteDocsWithIds(Seq("node:1001", "node:1002", "node:1004"))

      database.docWithId(doc1._id, classOf[NodeDoc]) should equal(None)
      database.docWithId(doc2._id, classOf[NodeDoc]) should equal(None)
      database.docWithId(doc3._id, classOf[NodeDoc]).get._id should equal(doc3._id)
    });
  }

}
