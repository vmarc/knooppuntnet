package kpn.core.tools

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import kpn.core.app.ActorSystemConfig
import kpn.core.changes.ChangeSetBuilder
import kpn.core.db.couch.Couch
import kpn.core.db.couch.DatabaseImpl
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.replicate.Oper
import kpn.core.tools.analyzer.AnalyzerEngineImpl
import kpn.core.tools.config.AnalysisToolConfiguration
import kpn.core.tools.config.Configuration
import kpn.core.util.Log
import kpn.shared.ReplicationId
import spray.can.Http
import spray.util.pimpFuture

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt

object AnalyzerTool {

  // number of seconds to wait before attempting to process new minute diff file, once in sync
  private val WAIT = 35

  // milliseconds between poll of shutdown flag during sleep
  private val SLEEP_SHUTDOWN_POLL_INTERVAL = 250L

  private val log = Log(classOf[AnalyzerTool])

  def main(args: Array[String]): Unit = {

    val exit = AnalyzerToolOptions.parse(args) match {
      case Some(options) =>

        val system = ActorSystemConfig.actorSystem()
        try {
          val configuration = buildConfiguration(system, options)
          new AnalyzerTool(configuration).analyze()
        }
        catch {
          case e: Throwable => log.fatal("Exception thrown during analysis", e)
        }
        finally {
          IO(Http)(system).ask(Http.CloseAll)(15.second).await
          Await.result(system.terminate(), Duration.Inf)
          log.info(s"Stopped")
          ()
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }

  private def buildConfiguration(system: ActorSystem, options: AnalyzerToolOptions): Configuration = {
    val couchConfig = Couch.config
    val couch = new Couch(system, couchConfig)
    val analysisDatabase = new DatabaseImpl(couch, options.analysisDatabaseName)
    val changeDatabase = new DatabaseImpl(couch, options.changeDatabaseName)
    val taskDatabase = new DatabaseImpl(couch, options.taskDatabaseName)
    new AnalysisToolConfiguration(
      system,
      analysisDatabase,
      changeDatabase,
      taskDatabase,
      options.initialDatabaseLoad
    ).config
  }
}

class AnalyzerTool(config: Configuration) {

  import AnalyzerTool._

  private val log = AnalyzerTool.log

  private val oper = new Oper()

  private val engine = new AnalyzerEngineImpl(config)

  def analyze(): Unit = {
    config.statusRepository.analysisStatus match {
      case None => log.error("Could not start: failed to read " + config.dirs.analysisStatus.getAbsolutePath)
      case Some(replicationId) => analyze(replicationId)
    }
  }

  private def analyze(previousReplicationId: ReplicationId): Unit = {
    config.analysisDatabaseIndexer.index()
    engine.load(previousReplicationId)
    processLoop(previousReplicationId)
  }

  @tailrec
  private def processLoop(previousReplicationId: ReplicationId): Unit = {

    val replicationId = previousReplicationId.next
    val updaterReplicationId = readUpdaterReplicationId()

    if (replicationId.number <= updaterReplicationId.number) {
      if (oper.isActive) {
        engine.process(replicationId)
        config.statusRepository.writeAnalysisStatus(replicationId)
        if (oper.isActive) {
          processLoop(replicationId)
        }
      }
    }
    else {
      // all available miniute diffs processed, sleep for a while; then try again
      sleep(previousReplicationId)
      if (oper.isActive) {
        processLoop(previousReplicationId)
      }
    }
  }

  private def process(replicationId: ReplicationId): Unit = {
    Log.context(s"${replicationId.name}") {
      log.debug("Start")
      log.elapsed {
        val osmChange = config.osmChangeRepository.get(replicationId)
        val timestamp = config.osmChangeRepository.timestamp(replicationId)
        val changeSets = ChangeSetBuilder.from(timestamp, osmChange)
        changeSets.zipWithIndex.foreach { case (changeSet, index) =>
          Log.context(s"${changeSet.id}") {
            val context = ChangeSetContext(replicationId, changeSet)
            config.changeProcessor.process(context)
          }
        }
        (osmChange.timestampFrom.map(_.iso).getOrElse(""), ())
      }
    }
  }

  private def sleep(replicationId: ReplicationId): Option[ReplicationId] = {
    if (oper.isActive) {
      sleep(WAIT)
      if (oper.isActive) {
        Some(replicationId)
      }
      else {
        None
      }
    }
    else {
      None
    }
  }

  private def sleep(seconds: Int): Unit = {
    log.info(s"Waiting ${seconds}s")
    val end = System.currentTimeMillis() + (seconds * 1000)
    while (oper.isActive && System.currentTimeMillis() < end) {
      Thread.sleep(SLEEP_SHUTDOWN_POLL_INTERVAL)
    }
    log.info(s"End waiting ${seconds}s")
  }

  private def readUpdaterReplicationId(): ReplicationId = {
    config.statusRepository.updaterStatus match {
      case Some(updaterReplicationId) => updaterReplicationId
      case None =>
        val message = "Could not read " + config.dirs.updateStatus.getAbsolutePath
        log.error(message)
        throw new RuntimeException(message)
    }
  }
}
