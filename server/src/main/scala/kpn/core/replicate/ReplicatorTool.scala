package kpn.core.replicate

import java.io.File

import kpn.api.common.ReplicationId
import kpn.api.common.status.ActionTimestamp
import kpn.core.action.ReplicationAction
import kpn.core.db.couch.Couch
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.core.util.GZipFile
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.OsmChangeReader
import kpn.server.repository.BackendActionsRepository
import kpn.server.repository.BackendMetricsRepositoryImpl

object ReplicatorTool {

  private val log = Log(classOf[ReplicatorTool])

  /*
   * Minimum number of seconds between pairs of requests to OSM (to prevent overload on API). This wait time will only
   * be used when we are in "catch up" mode. Once we are in sync, then the wait time will become longer (see WAIT).
   */
  private val DELAY = 5

  /*
   * Number of seconds to wait before attempting to make new request to osm API when in sync. New minute diffs should
   * become available every 60 seconds. We do not set the wait time to 60S, because this would cause us to gradually
   * get more and more behind. A wait time of 35 seconds means that we should normally get one failed attempt and one
   * successfull attempt per minute once we are in sync.
   */
  private val WAIT = 35

  def main(args: Array[String]): Unit = {

    val exit = ReplicatorToolOptions.parse(args) match {
      case Some(options) =>

        Couch.executeIn(Couch.config.host, options.actionsDatabaseName) { actionsDatabase =>
          val dirs = Dirs()

          try {
            val statusRepository = new StatusRepositoryImpl(dirs)
            val replicationStateRepository = new ReplicationStateRepositoryImpl(dirs.replicate)
            val replicationRequestExecutor = new ReplicationRequestExecutorImpl()
            val actionsRepository = new BackendMetricsRepositoryImpl(actionsDatabase)
            new ReplicatorTool(
              dirs.replicate,
              statusRepository,
              replicationStateRepository,
              replicationRequestExecutor,
              actionsRepository
            ).launch()
          }
          finally {
            log.info("Ended")
          }
        }
        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }
}

private object ReplicationResultCode extends Enumeration {
  val Ok, NotFound, Error, End = Value
}

private case class ReplicationResult(
  code: ReplicationResultCode.Value,
  fileSize: Long = 0,
  elementCount: Long = 0,
  changeSetCount: Long = 0
)

import kpn.core.replicate.ReplicationResultCode._

class ReplicatorTool(
  replicateDir: File,
  statusRepository: StatusRepositoryImpl,
  replicationStateRepository: ReplicationStateRepository,
  replicationRequestExecutor: ReplicationRequestExecutor,
  actionsRepository: BackendActionsRepository
) {

  private val log = ReplicatorTool.log

  private val oper = new Oper()

  def launch(): Unit = {
    statusRepository.replicatorStatus match {
      case None => log.error("Cannot find current replication status")
      case Some(replicationId) =>
        log.info("Start replication id=" + replicationId.name)
        launch(replicationId)
    }
  }

  private def launch(initialReplicationId: ReplicationId): Unit = {

    var insync = false
    var inerror = false
    var replicationId = initialReplicationId.next

    while (oper.isActive) {

      Log.context(replicationId.name) {

        try {
          replicate(replicationId) match {
            case ReplicationResult(Ok, fileSize, elementCount, changeSetCount) =>
              statusRepository.writeReplicationStatus(replicationId)
              val timestamp = replicationStateRepository.read(replicationId)
              val minuteDiffInfo = ActionTimestamp.minuteDiffInfo(replicationId.number, timestamp)
              actionsRepository.saveReplicationAction(
                ReplicationAction(
                  minuteDiffInfo,
                  fileSize,
                  elementCount,
                  changeSetCount
                )
              )
              log.info(s"OK ${timestamp.yyyymmddhhmmss}")

              replicationId = replicationId.next
              /*
                We have successfully replicated the files for the current replication id. If we were in error mode
                before, we are no longer in error mode now.
               */
              inerror = false

            case ReplicationResult(NotFound, _, _, _) =>
              /*
                The OpenStreetMap server told us that the files for the current replication id do not exist (yet). We
                assume that we have replicated all available files and wait for a longer time for new files to become
                available.
               */
              insync = true

            case ReplicationResult(Error, _, _, _) =>
              /*
                An error occurred. We switch to error mode (with longer waiting time between retries), and we assume
                that we will no longer be in sync after we recover from the error mode.
               */
              inerror = true
              insync = false

            case ReplicationResult(End, _, _, _) =>
          }
        }
        catch {
          case e: Exception =>
            log.error(e.getMessage)
            insync = false
            inerror = true
        }
      }

      if (oper.isActive) {
        if (insync || inerror) {
          sleep(ReplicatorTool.WAIT)
        } else {
          sleep(ReplicatorTool.DELAY)
        }
      }
    }
  }

  private def replicate(replicationId: ReplicationId): ReplicationResult = {
    val result = replicateChangesFile(replicationId)
    result match {
      case ReplicationResult(Ok, _, _, _) =>
        if (oper.isActive) {
          if (!replicateStateFile(replicationId)) {
            ReplicationResult(NotFound)
          }
          else {
            result
          }
        }
        else {
          ReplicationResult(End)
        }
      case result => result
    }
  }

  private def replicateChangesFile(replicationId: ReplicationId): ReplicationResult = {
    replicationRequestExecutor.requestChangesFile(replicationId) match {
      case None =>
        ReplicationResult(NotFound)

      case Some(changesString) =>
        val file = new File(replicateDir + "/" + replicationId.name + ".osc.gz")
        file.getParentFile.mkdirs()
        GZipFile.write(file.getAbsolutePath, changesString)
        try {
          val osmChange = new OsmChangeReader(file.getAbsolutePath).read
          log.debug(file.getAbsolutePath + " integrity check OK")
          ReplicationResult(
            Ok,
            file.length(),
            osmChange.allElementIds.size,
            osmChange.allChangeSetIds.size
          )
        }
        catch {
          case e: Exception =>
            log.error(file.getAbsolutePath + " integrity check 2 NOK", e)
            ReplicationResult(Error)
        }
    }
  }

  private def replicateStateFile(replicationId: ReplicationId): Boolean = {
    replicationRequestExecutor.requestStateFile(replicationId) match {
      case None => false
      case Some(stateString) =>
        replicationStateRepository.write(replicationId, stateString)
        true
    }
  }

  private def sleep(seconds: Int): Unit = {
    log.debug("sleep " + seconds + "s")
    val end = System.currentTimeMillis() + (seconds * 1000)
    while (oper.isActive && System.currentTimeMillis() < end) {
      Thread.sleep(1000)
    }
  }
}
