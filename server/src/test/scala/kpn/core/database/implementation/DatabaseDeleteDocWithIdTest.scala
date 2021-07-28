package kpn.core.database.implementation

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.database.doc.CouchNodeDoc
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.test.TestSupport.withEnvironment
import kpn.core.util.UnitTest

class DatabaseDeleteDocWithIdTest extends UnitTest with TestObjects {

  test("delete document") {

    withCouchDatabase { database =>

      val nodeInfo = newNodeInfo(123)
      val doc = CouchNodeDoc("123", nodeInfo, None)

      database.save(doc)
      database.docWithId(doc._id, classOf[CouchNodeDoc]).get.node.id should equal(123)

      database.deleteDocWithId(doc._id)

      database.docWithId(doc._id, classOf[CouchNodeDoc]) should equal(None)
    }
  }

  test("delete - wrong password") {
    withEnvironment { (couchConfig, objectMapper) =>
      val databaseName = s"test-db-${UUID.randomUUID().toString}"
      val database = new DatabaseImpl(DatabaseContextImpl(couchConfig, objectMapper, databaseName))
      database.create()
      try {
        val nodeInfo = newNodeInfo(123)
        val nodeDoc = CouchNodeDoc("123", nodeInfo, None)
        database.save(nodeDoc)

        val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
        val otherDatabase = new DatabaseImpl(DatabaseContextImpl(invalidCouchConfig, objectMapper, databaseName))

        try {
          otherDatabase.deleteDocWithId(nodeDoc._id)
          fail("Expected exception not thrown")
        }
        catch {
          case e: IllegalStateException =>
            e.getMessage should include("Could not get document")
            e.getMessage should include("(invalid user/password?)")
        }
      }
      finally {
        database.delete()
      }
    }
  }

  test("delete - no exception when document does not exist") {
    withCouchDatabase { database =>
      database.deleteDocWithId("bla")
    }
  }

}
