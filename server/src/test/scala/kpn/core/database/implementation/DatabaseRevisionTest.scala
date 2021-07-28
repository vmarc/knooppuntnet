package kpn.core.database.implementation

import kpn.core.TestObjects
import kpn.core.database.doc.CouchNodeDoc
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class DatabaseRevisionTest extends UnitTest with TestObjects {

  test("revision - get document revision without having to load the entire document") {

    withCouchDatabase { database =>

      val doc = {
        val nodeInfo = newNodeInfo(123)
        CouchNodeDoc("123", nodeInfo, None)
      }

      database.save(doc)

      val rev = database.docWithId(doc._id, classOf[CouchNodeDoc]).flatMap(_._rev)

      database.revision(doc._id) should equal(rev)
    }
  }

  test("revision - None if non-existing document") {
    withCouchDatabase { database =>
      database.revision("bla") should equal(None)
    }
  }
}
