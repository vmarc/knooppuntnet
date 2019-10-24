package kpn.core.db.couch.implementation

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.db.NodeDoc
import kpn.core.db.StringValueDoc
import kpn.core.db.couch.Database
import kpn.core.db.couch.DatabaseImpl
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.TestSupport.withEnvironment
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseSaveTest extends FunSuite with Matchers with TestObjects {

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
    withEnvironment((couchConfig, objectMapper) => {
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
    })
  }

  test("save - document already exists") {

    withDatabase(database => {

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
    })
  }

}
