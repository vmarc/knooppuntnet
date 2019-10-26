package kpn.core.database.implementation

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.database.doc.NodeDoc
import kpn.core.database.{Database, DatabaseImpl}
import kpn.core.test.TestSupport.{withDatabase, withEnvironment}
import org.scalatest.{FunSuite, Matchers}

class DatabaseDeleteDocWithIdTest extends FunSuite with Matchers with TestObjects {

  test("delete document") {

    withDatabase(database => {

      val nodeInfo = newNodeInfo(123)
      val doc = NodeDoc("123", nodeInfo, None)

      database.save(doc)
      database.docWithId(doc._id, classOf[NodeDoc]).get.node.id should equal(123)

      database.deleteDocWithId(doc._id)

      database.docWithId(doc._id, classOf[NodeDoc]) should equal(None)
    });
  }

  test("delete - wrong password") {
    withEnvironment((tempCouch, couchConfig, objectMapper) => {
      val databaseName = s"test-db-${UUID.randomUUID().toString}"
      val database: Database = new DatabaseImpl(DatabaseContext(tempCouch, couchConfig, objectMapper, databaseName))
      database.create()
      try {
        val nodeInfo = newNodeInfo(123)
        val nodeDoc = NodeDoc("123", nodeInfo, None)
        database.save(nodeDoc)

        val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
        val otherDatabase: Database = new DatabaseImpl(DatabaseContext(tempCouch, invalidCouchConfig, objectMapper, databaseName))

        try {
          otherDatabase.deleteDocWithId(nodeDoc._id)
          fail("Expected exception not thrown")
        }
        catch {
          case e: IllegalStateException =>
            e.getMessage should include("Could not delete")
            e.getMessage should include("(invalid user/password?)")
        }
      }
      finally {
        database.delete()
      }
    })
  }

  test("delete - no exception when document does not exist") {
    withDatabase(database => {
      database.deleteDocWithId("bla")
    });
  }

}
