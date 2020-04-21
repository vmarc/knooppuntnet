package kpn.core.database.implementation

import kpn.core.TestObjects
import kpn.core.database.doc.NodeDoc
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class DatabaseBulkSaveTest extends AnyFunSuite with Matchers with TestObjects {

  test("bulkSave") {

    withDatabase(database => {

      val doc1 = NodeDoc("node:1001", newNodeInfo(1001), None)
      val doc2 = NodeDoc("node:1002", newNodeInfo(1002), None)
      val doc3 = NodeDoc("node:1003", newNodeInfo(1003), None)

      database.bulkSave(Seq(doc1, doc2, doc3))

      database.docWithId(doc1._id, classOf[NodeDoc]).map(_._id) should equal(Some(doc1._id))
      database.docWithId(doc2._id, classOf[NodeDoc]).map(_._id) should equal(Some(doc2._id))
      database.docWithId(doc3._id, classOf[NodeDoc]).map(_._id) should equal(Some(doc3._id))
    });
  }

}
