package kpn.core.database.implementation

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.database.doc.NodeDoc
import kpn.core.database.doc.StringValueDoc
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.TestSupport.withEnvironment
import kpn.core.util.UnitTest

class DatabaseSaveTest extends UnitTest with TestObjects {

  test("save") {

    withDatabase(database => {

      val nodeInfo = newNodeInfo(123)
      val doc = NodeDoc("123", nodeInfo, None)

      database.docWithId(doc._id, classOf[NodeDoc]) should equal(None)

      database.save(doc)

      database.docWithId(doc._id, classOf[NodeDoc]) match {
        case Some(result) => result.node should equal(nodeInfo)
      }
    });
  }

  test("save - wrong password") {
    withEnvironment { (couchConfig, objectMapper) =>
      val databaseName = s"test-db-${UUID.randomUUID().toString}"
      val database: Database = new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, databaseName))
      database.create()
      try {
        val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
        val database: Database = new DatabaseImpl(DatabaseContext(invalidCouchConfig, objectMapper, databaseName))

        try {
          val nodeInfo = newNodeInfo(123)
          val nodeDoc = NodeDoc("123", nodeInfo, None)
          database.save(nodeDoc)
          fail("Expected exception not thrown")
        }
        catch {
          case e: IllegalStateException =>
            e.getMessage should include("Could not save")
            e.getMessage should include("(invalid user/password?)")
        }
      }
      finally {
        database.delete()
      }
    }
  }

  test("save - document already exists") {

    withDatabase { database =>

      val nodeInfo = newNodeInfo(123)
      val doc = NodeDoc("123", nodeInfo, None)

      database.docWithId(doc._id, classOf[NodeDoc]) should equal(None)

      database.save(doc)

      try {
        database.save(doc)
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage should include("Could not save")
          e.getMessage should include("(_rev mismatch)")
      }
    }
  }

  test("save same document multiple times") {
    withDatabase { database =>

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
