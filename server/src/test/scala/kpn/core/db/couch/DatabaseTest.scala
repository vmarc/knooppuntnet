package kpn.core.db.couch

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.db.NodeDoc
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.TestSupport.withEnvironment
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseTest extends FunSuite with Matchers with TestObjects {

  test("exists") {

    withDatabase(database => {
      database.exists should equal(true)
    })

    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(couchConfig, objectMapper, "bla")
      database.exists should equal(false)
    })
  }

  test("create") {
    withDatabase(database => {
      database.exists should equal(true)
    })
  }

  test("create - database already exists") {

    val databaseName = s"test-db-${UUID.randomUUID().toString}"

    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(couchConfig, objectMapper, databaseName)
      database.exists should equal(false)
      database.create()
      database.exists should equal(true)
      try {
        database.create()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage.contains("Could not create database") should equal(true)
          e.getMessage.contains("(already exists?)") should equal(true)
      }
      finally {
        database.delete()
      }
    })
  }

  test("create - invalid authorization") {

    val databaseName = s"test-db-${UUID.randomUUID().toString}"

    withEnvironment((couchConfig, objectMapper) => {
      val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
      val database: Database = new DatabaseImpl(invalidCouchConfig, objectMapper, "databaseName")
      database.exists should equal(false)
      try {
        database.create()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage.contains("Could not create database") should equal(true)
          e.getMessage.contains("(invalid user/password?)") should equal(true)
      }
    })
  }

  test("delete") {
    val databaseName = s"test-db-${UUID.randomUUID().toString}"
    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(couchConfig, objectMapper, databaseName)
      database.create()
      database.exists should equal(true)
      database.delete()
      database.exists should equal(false)
    })
  }

  test("delete - database does not exist") {
    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(couchConfig, objectMapper, "bla")
      database.exists should equal(false)
      try {
        database.delete()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage.contains("Could not delete database") should equal(true)
          e.getMessage.contains("(database does not exist?)") should equal(true)
      }
    })
  }

  test("delete - wrong password") {
    withEnvironment((couchConfig, objectMapper) => {
      val databaseName = s"test-db-${UUID.randomUUID().toString}"
      val originalDatabase: Database = new DatabaseImpl(couchConfig, objectMapper, databaseName)
      originalDatabase.create()
      try {
        val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
        val database: Database = new DatabaseImpl(invalidCouchConfig, objectMapper, databaseName)
        database.exists should equal(true)
        try {
          database.delete()
          fail("Expected exception not thrown")
        }
        catch {
          case e: IllegalStateException =>
            e.getMessage.contains("Could not delete database") should equal(true)
            e.getMessage.contains("(invalid user/password?)") should equal(true)
        }
      }
      finally {
        originalDatabase.delete()
      }
    })
  }

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
      val database: Database = new DatabaseImpl(couchConfig, objectMapper, databaseName)
      database.create()
      try {
        val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
        val database: Database = new DatabaseImpl(invalidCouchConfig, objectMapper, databaseName)

        try {
          val nodeInfo = newNodeInfo(123)
          val nodeDoc = NodeDoc("123", nodeInfo, None)
          database.save(nodeDoc)
          fail("Expected exception not thrown")
        }
        catch {
          case e: IllegalStateException =>
            e.getMessage.contains("Could not save") should equal(true)
            e.getMessage.contains("(invalid user/password?)") should equal(true)
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
          e.getMessage.contains("Could not save") should equal(true)
          e.getMessage.contains("(_rev mismatch)") should equal(true)
      }
    });
  }

  test("revision") {

    withDatabase(database => {

      val doc = {
        val nodeInfo = newNodeInfo(123)
        NodeDoc("123", nodeInfo, None)
      }

      database.save(doc)

      val rev = database.docWithId(doc._id, classOf[NodeDoc]).flatMap(_._rev)

      database.revision(doc._id) should equal(rev)

    });
  }

  test("revision - non-existing document") {
    withDatabase(database => {
      database.revision("bla") should equal(None)
    });
  }

}
