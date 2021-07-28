package kpn.core.database.implementation

import kpn.core.TestObjects
import kpn.core.database.doc.CouchNodeDoc
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class DatabaseBulkSaveTest extends UnitTest with TestObjects {

  test("bulkSave") {

    withCouchDatabase { database =>

      val doc1 = CouchNodeDoc("node:1001", newNodeInfo(1001), None)
      val doc2 = CouchNodeDoc("node:1002", newNodeInfo(1002), None)
      val doc3 = CouchNodeDoc("node:1003", newNodeInfo(1003), None)

      database.bulkSave(Seq(doc1, doc2, doc3))

      database.docWithId(doc1._id, classOf[CouchNodeDoc]).map(_._id) should equal(Some(doc1._id))
      database.docWithId(doc2._id, classOf[CouchNodeDoc]).map(_._id) should equal(Some(doc2._id))
      database.docWithId(doc3._id, classOf[CouchNodeDoc]).map(_._id) should equal(Some(doc3._id))
    }
  }
}
