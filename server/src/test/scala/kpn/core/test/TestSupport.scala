package kpn.core.test

import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import org.mongodb.scala.MongoClient
import org.scalatest.Assertions

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TestSupport extends Assertions {

  private val count = new AtomicInteger(0)

  /**
   * Perform given function with a freshly created database. The database is deleted
   * afterwards.
   */
  def withDatabase(f: Database => Unit): Unit = {
    withDatabase(keepDatabaseAfterTest = false)(f)
  }

  /**
   * Perform given function with a freshly created database.
   */
  def withDatabase(keepDatabaseAfterTest: Boolean = false)(f: Database => Unit): Unit = {

    val databaseName = "unit-testdb-" + count.incrementAndGet()
    val mongoClient = MongoClient()
    try {
      val database = Mongo.database(mongoClient, databaseName)
      Await.result(database.database.drop().toFuture(), Duration(2, TimeUnit.SECONDS))
      try {
        f(database)
      } finally {
        if (!keepDatabaseAfterTest) {
          Await.result(database.database.drop().toFuture(), Duration(2, TimeUnit.SECONDS))
        }
      }
    }
    finally {
      mongoClient.close()
    }
  }

  /**
   * Perform given function with a freshly created database. The database is deleted
   * afterwards.
   */
  def withCouchDatabase(f: kpn.core.database.Database => Unit): Unit = {
  }

  /**
   * Perform given function with a freshly created database.
   */
  def withCouchDatabase(keepDatabaseAfterTest: Boolean = false)(f: kpn.core.database.Database => Unit): Unit = {
  }
}
