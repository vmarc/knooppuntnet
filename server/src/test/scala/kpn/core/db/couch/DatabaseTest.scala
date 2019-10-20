package kpn.core.db.couch

import java.util.UUID

import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.TestSupport.withEnvironment
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseTest extends FunSuite with Matchers {

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

}
