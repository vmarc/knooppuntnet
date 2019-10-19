package kpn.core.test

import java.io.File
import java.util.Properties
import java.util.concurrent.atomic.AtomicInteger

import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.config.ConfigFactory
import kpn.core.app.ActorSystemConfig
import kpn.core.db.couch.Couch
import kpn.core.db.couch.CouchConfig
import kpn.core.db.couch.OldDatabase
import kpn.core.db.couch.OldDatabaseImpl
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.ChangesDesign
import kpn.core.db.views.LocationDesign
import kpn.core.db.views.PlannerDesign
import kpn.server.repository.DesignRepositoryImpl
import org.scalatest.Assertions

import scala.concurrent.Await
import scala.concurrent.Awaitable
import scala.concurrent.duration.Duration
import scala.concurrent.duration.SECONDS
import scala.io.Source

object TestSupport extends Assertions {

  private val count = new AtomicInteger(0)

  def sync[T](awaitable: Awaitable[T]): T = {
    Await.result(awaitable, Duration(3, SECONDS))
  }

  private var couch: Option[Couch] = None

  /**
   * Perform given function with a freshly created database. The database is deleted
   * afterwards.
   */
  def withOldDatabase(f: OldDatabase => Unit): Unit = {
    withOldDatabase(keepDatabaseAfterTest = false)(f: OldDatabase => Unit)
  }

  /**
   * Perform given function with a freshly created database.
   */
  def withOldDatabase(keepDatabaseAfterTest: Boolean = false)(f: OldDatabase => Unit): Unit = {

    withOldCouch { c =>
      val dbname = "unit-testdb-" + count.incrementAndGet()

      val database = new OldDatabaseImpl(c, dbname)

      if (database.exists) {
        database.delete()
      }

      database.create()
      new DesignRepositoryImpl(database).save(AnalyzerDesign)
      new DesignRepositoryImpl(database).save(ChangesDesign)
      new DesignRepositoryImpl(database).save(PlannerDesign)
      new DesignRepositoryImpl(database).save(LocationDesign)

      try {
        f(database)
      } finally {
        if (!keepDatabaseAfterTest) {
          database.delete()
        }
      }
    }
  }

  def withOldCouch(action: Couch => Unit): Unit = {

    if (couch.isEmpty) {
      val system = ActorSystemConfig.actorSystem()

      val properties = new File(System.getProperty("user.home") + "/.osm/osm.properties")
      val config = ConfigFactory.parseFile(properties)
      val user = config.getString("couchdb.user")
      val password = config.getString("couchdb.password")
      val host = config.getString("couchdb.host")
      val port = config.getInt("couchdb.port")

      val couchConfig = CouchConfig(host, port, user, password)
      couch = Some(new Couch(system, couchConfig))
    }

    action(couch.get)
  }

  // =====================

  def withEnvironment(action: (CouchConfig, ObjectMapper) => Unit): Unit = {

    val properties: Properties = new Properties()
    val source = Source.fromFile("/kpn/conf/test.properties")
    properties.load(source.bufferedReader())

    val user = properties.getProperty("couch.user")
    val password = properties.getProperty("couch.password")
    val host = properties.getProperty("couch.host")
    val port = properties.getProperty("couch.port").toInt

    val couchConfig = CouchConfig(host, port, user, password)

    val objectMapper = new ObjectMapper
    action(couchConfig, objectMapper)
  }

}
