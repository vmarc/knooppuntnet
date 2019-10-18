package kpn.core.test

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
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
  def withDatabase(f: OldDatabase => Unit): Unit = {
    withDatabase(keepDatabaseAfterTest = false)(f: OldDatabase => Unit)
  }

  /**
    * Perform given function with a freshly created database.
    */
  def withDatabase(keepDatabaseAfterTest: Boolean = false)(f: OldDatabase => Unit): Unit = {

    withCouch { c =>
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

  def withCouch(action: Couch => Unit): Unit = {

    if (couch.isEmpty) {
      val system = ActorSystemConfig.actorSystem()

      val properties = new File(System.getProperty("user.home") + "/.osm/osm.properties")
      val config = ConfigFactory.parseFile(properties)
      val user = config.getString("couchdb.user")
      val password = config.getString("couchdb.password")
      val host = config.getString("couchdb.host")
      val port = config.getInt("couchdb.port")

      val couchConfig = CouchConfig(host, port, user, password, "", "", "", "", "")
      couch = Some(new Couch(system, couchConfig))
    }

    action(couch.get)
  }
}
