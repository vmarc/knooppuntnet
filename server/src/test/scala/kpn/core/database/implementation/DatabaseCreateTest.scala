package kpn.core.database.implementation

import kpn.core.TestObjects
import kpn.core.database.DatabaseImpl
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.TestSupport.withEnvironment
import kpn.core.util.UnitTest

import java.util.UUID

class DatabaseCreateTest extends UnitTest with TestObjects {

  test("create") {
    withDatabase { database =>
      assert(database.exists)
    }
  }

  test("create - database already exists") {

    val databaseName = s"test-db-${UUID.randomUUID().toString}"

    withEnvironment { (couchConfig, objectMapper) =>
      val database = new DatabaseImpl(DatabaseContextImpl(couchConfig, objectMapper, databaseName))
      assert(!database.exists)
      database.create()
      assert(database.exists)
      try {
        database.create()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage should include("Could not create database")
          e.getMessage should include("(already exists?)")
      }
      finally {
        database.delete()
      }
    }
  }

  test("create - invalid authorization") {

    val databaseName = s"test-db-${UUID.randomUUID().toString}"

    withEnvironment { (couchConfig, objectMapper) =>
      val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
      val database = new DatabaseImpl(DatabaseContextImpl(invalidCouchConfig, objectMapper, databaseName))
      try {
        database.create()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage should include("Could not create database")
          e.getMessage should include("(invalid user/password?)")
      }
    }
  }
}
