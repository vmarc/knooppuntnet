package kpn.core.test

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContextImpl
import kpn.core.database.views.analyzer.AnalyzerDesign
import kpn.core.database.views.changes.ChangeDocumentsDesign
import kpn.core.database.views.changes.ChangesDesign
import kpn.core.database.views.location.LocationDesign
import kpn.core.database.views.monitor.MonitorDesign
import kpn.core.database.views.node.NodeRouteDesign
import kpn.core.database.views.planner.PlannerDesign
import kpn.core.database.views.poi.PoiDesign
import kpn.core.database.views.tile.TileDesign
import kpn.core.db.couch.CouchConfig
import kpn.core.mongo.util.Mongo.codecRegistry
import kpn.server.json.Json
import kpn.server.repository.DesignRepositoryImpl
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase
import org.scalatest.Assertions

import java.util.Properties
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source

object TestSupport extends Assertions {

  private val count = new AtomicInteger(0)

  def withEnvironment(action: (CouchConfig, ObjectMapper) => Unit): Unit = {
    val couchConfig = readCouchConfig()
    action(couchConfig, Json.objectMapper)
  }

  /**
   * Perform given function with a freshly created database. The database is deleted
   * afterwards.
   */
  def withDatabase(f: MongoDatabase => Unit): Unit = {
    withDatabase(keepDatabaseAfterTest = false)(f: MongoDatabase => Unit)
  }

  /**
   * Perform given function with a freshly created database.
   */
  def withDatabase(keepDatabaseAfterTest: Boolean = false)(f: MongoDatabase => Unit): Unit = {

    val databaseName = "unit-testdb-" + count.incrementAndGet()
    val mongoClient = MongoClient()
    try {
      val database = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry)
      Await.result(database.drop().toFuture(), Duration(2, TimeUnit.SECONDS))
      try {
        f(database)
      } finally {
        if (!keepDatabaseAfterTest) {
          Await.result(database.drop().toFuture(), Duration(2, TimeUnit.SECONDS))
        }
      }
    }
  }

  /**
   * Perform given function with a freshly created database. The database is deleted
   * afterwards.
   */
  def withCouchDatabase(f: Database => Unit): Unit = {
    withCouchDatabase(keepDatabaseAfterTest = false)(f: Database => Unit)
  }

  /**
   * Perform given function with a freshly created database.
   */
  def withCouchDatabase(keepDatabaseAfterTest: Boolean = false)(f: Database => Unit): Unit = {

    withEnvironment { (couchConfig, objectMapper) =>

      val databaseName = "unit-testdb-" + count.incrementAndGet()

      val database = new DatabaseImpl(DatabaseContextImpl(couchConfig, objectMapper, databaseName))

      if (database.exists) {
        database.delete()
      }

      database.create()

      new DesignRepositoryImpl(database).save(AnalyzerDesign)
      new DesignRepositoryImpl(database).save(ChangesDesign)
      new DesignRepositoryImpl(database).save(ChangeDocumentsDesign)
      new DesignRepositoryImpl(database).save(PlannerDesign)
      new DesignRepositoryImpl(database).save(LocationDesign)
      new DesignRepositoryImpl(database).save(PoiDesign)
      new DesignRepositoryImpl(database).save(TileDesign)
      new DesignRepositoryImpl(database).save(NodeRouteDesign)
      new DesignRepositoryImpl(database).save(MonitorDesign)

      try {
        f(database)
      } finally {
        if (!keepDatabaseAfterTest) {
          database.delete()
        }
      }
    }
  }

  private def readCouchConfig(): CouchConfig = {

    val properties: Properties = new Properties()
    val source = Source.fromFile("/kpn/conf/test.properties")
    properties.load(source.bufferedReader())

    val user = properties.getProperty("couch.user")
    val password = properties.getProperty("couch.password")
    val host = properties.getProperty("couch.host")
    val port = properties.getProperty("couch.port").toInt

    CouchConfig(host, port, user, password)
  }

}
