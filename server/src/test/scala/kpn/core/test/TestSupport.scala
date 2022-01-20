package kpn.core.test

import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.MongoClient
import org.scalatest.Assertions

import java.util.concurrent.atomic.AtomicInteger

object TestSupport extends Assertions {

  private val count = new AtomicInteger(0)

  /**
   * Perform given function with a freshly created database. The database is deleted
   * afterwards.
   */
  def withDatabase(f: Database => Unit): Unit = {
    withDatabase()(f)
  }

  /**
   * Perform given function with a freshly created database.
   */
  def withDatabase(keepDatabaseAfterTest: Boolean = false)(f: Database => Unit): Unit = {

    val databaseName = "unit-testdb-" + count.incrementAndGet()
    val mongoClient = MongoClient()
    try {
      val database = Mongo.database(mongoClient, databaseName)
      database.dropDatabase()
      try {
        f(database)
      } finally {
        if (!keepDatabaseAfterTest) {
          database.dropDatabase()
        }
      }
    }
    finally {
      mongoClient.close()
    }
  }
}
