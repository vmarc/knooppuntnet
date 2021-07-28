package kpn.core.database.implementation

import kpn.core.TestObjects
import kpn.core.database.DatabaseImpl
import kpn.core.database.doc.CouchNodeDoc
import kpn.core.database.doc.StringValueDoc
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.test.TestSupport.withEnvironment
import kpn.core.util.UnitTest

import java.util.UUID

class DatabaseSaveTest extends UnitTest with TestObjects {

  test("save") {

    withCouchDatabase { database =>

      val nodeInfo = newNodeInfo(123)
      val doc = CouchNodeDoc("123", nodeInfo, None)

      database.docWithId(doc._id, classOf[CouchNodeDoc]) should equal(None)

      database.save(doc)

      database.docWithId(doc._id, classOf[CouchNodeDoc]) match {
        case Some(result) => result.node should equal(nodeInfo)
        case None => fail()
      }
    }
  }

  test("save - wrong password") {
    withEnvironment { (couchConfig, objectMapper) =>
      val databaseName = s"test-db-${UUID.randomUUID().toString}"
      val database = new DatabaseImpl(DatabaseContextImpl(couchConfig, objectMapper, databaseName))
      database.create()
      try {
        val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
        val database = new DatabaseImpl(DatabaseContextImpl(invalidCouchConfig, objectMapper, databaseName))

        try {
          val nodeInfo = newNodeInfo(123)
          val nodeDoc = CouchNodeDoc("123", nodeInfo, None)
          database.save(nodeDoc)
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

  test("save - document already exists") {

    withCouchDatabase { database =>

      val nodeInfo = newNodeInfo(123)
      val doc = CouchNodeDoc("123", nodeInfo, None)

      database.docWithId(doc._id, classOf[CouchNodeDoc]) should equal(None)

      database.save(doc)
      database.save(doc) // without retries, this would result in update conflict
    }
  }

  test("save same document multiple times") {
    withCouchDatabase { database =>

      database.save(StringValueDoc("id1", "value1"))

      val doc1 = database.docWithId("id1", classOf[StringValueDoc])
      doc1.map(_.value) should equal(Some("value1"))

      database.save(StringValueDoc("id1", "value2", doc1.get._rev))
      val doc2 = database.docWithId("id1", classOf[StringValueDoc])
      doc2.map(_.value) should equal(Some("value2"))

      doc1.get._rev should not equal (doc2.get._rev)
    }
  }
}
