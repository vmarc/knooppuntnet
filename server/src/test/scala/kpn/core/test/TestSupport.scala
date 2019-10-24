package kpn.core.test

import java.util.Properties
import java.util.concurrent.atomic.AtomicInteger

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.core.app.ActorSystemConfig
import kpn.core.db.couch.Couch
import kpn.core.db.couch.CouchConfig
import kpn.core.db.couch.Database
import kpn.core.db.couch.DatabaseImpl
import kpn.core.db.couch.implementation.DatabaseContext
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.ChangesDesign
import kpn.core.db.views.LocationDesign
import kpn.core.db.views.PlannerDesign
import kpn.server.repository.DesignRepositoryImpl
import org.scalatest.Assertions

import scala.io.Source

object TestSupport extends Assertions {

  private val count = new AtomicInteger(0)

  private var tempCouch: Option[Couch] = None

  def withEnvironment(action: (Couch, CouchConfig, ObjectMapper) => Unit): Unit = {
    val couchConfig = readCouchConfig()
    if (tempCouch.isEmpty) {
      val system = ActorSystemConfig.actorSystem()
      tempCouch = Some(new Couch(system, couchConfig))
    }
    action(tempCouch.get, couchConfig, Couch.objectMapper)
  }

  /**
   * Perform given function with a freshly created database. The database is deleted
   * afterwards.
   */
  def withDatabase(f: Database => Unit): Unit = {
    withDatabase(keepDatabaseAfterTest = false)(f: Database => Unit)
  }

  /**
   * Perform given function with a freshly created database.
   */
  def withDatabase(keepDatabaseAfterTest: Boolean = false)(f: Database => Unit): Unit = {

    withEnvironment { (tempCouch, couchConfig, objectMapper) =>

      val databaseName = "unit-testdb-" + count.incrementAndGet()

      val database = new DatabaseImpl(DatabaseContext(tempCouch, couchConfig, objectMapper, databaseName))

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
