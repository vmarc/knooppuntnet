package kpn.core.tools

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import kpn.core.app.ActorSystemConfig
import kpn.core.common.TimestampUtil
import kpn.core.db.couch.Couch
import kpn.core.db.couch.DatabaseImpl
import kpn.core.replicate.Oper
import kpn.core.tools.analyzer.AnalyzerEngineImpl
import kpn.core.tools.config.ChangesToolConfiguration
import kpn.core.tools.config.Configuration
import kpn.core.util.Log
import kpn.shared.ReplicationId
import spray.can.Http
import spray.util.pimpFuture

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt

object ChangesTool {

  def main(args: Array[String]): Unit = {

    val exit = ChangesToolOptions.parse(args) match {
      case Some(options) =>

        val system = ActorSystemConfig.actorSystem()
        try {
          val config = buildConfiguration(system, options)
          val begin = ReplicationId(options.beginReplicationId)
          val end = ReplicationId(options.endReplicationId)
          new ChangesTool(config).analyze(begin, end)
        }
        finally {
          IO(Http)(system).ask(Http.CloseAll)(15.second).await
          Await.result(system.terminate(), Duration.Inf)
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }

  private def buildConfiguration(system: ActorSystem, options: ChangesToolOptions): Configuration = {
    val couchConfig = Couch.config
    val couch = new Couch(system, couchConfig)
    val analysisDatabase = new DatabaseImpl(couch, options.analysisDatabaseName)
    val changeDatabase = new DatabaseImpl(couch, options.changeDatabaseName)
    val taskDatabase = new DatabaseImpl(couch, options.taskDatabaseName)
    new ChangesToolConfiguration(system, analysisDatabase, changeDatabase, taskDatabase).config
  }
}

class ChangesTool(config: Configuration) {

  private val log = Log(classOf[ChangesTool])

  private val engine = new AnalyzerEngineImpl(config)

  private val oper = new Oper()

  def analyze(begin: ReplicationId, end: ReplicationId): Unit = {
    load(begin)
    log.debug(s"Starting to process")
    ReplicationId.range(begin, end).foreach { replicationId =>
      if (oper.isActive) {
        engine.process(replicationId)
        config.statusRepository.writeChangesStatus(replicationId)
      }
    }
  }

  private def load(begin: ReplicationId): Unit = {
    val beginOsmChange = config.osmChangeRepository.get(begin)
    val beginTimestamp = TimestampUtil.relativeSeconds(beginOsmChange.timestampFrom.get, -1)
    config.analysisDataLoader.load(beginTimestamp)
  }
}
