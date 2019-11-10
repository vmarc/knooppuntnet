package kpn.core.replicate

import java.io.File

import kpn.api.common.ReplicationId
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.core.util.GZipFile
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.OsmChangeReader

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

    val dirs = Dirs()

    try {
      val statusRepository = new StatusRepositoryImpl(dirs)
      val replicationStateRepository = new ReplicationStateRepositoryImpl(dirs.replicate)
      val replicationRequestExecutor = new ReplicationRequestExecutorImpl()
      new ReplicatorTool(
        dirs.replicate,
        statusRepository,
        replicationStateRepository,
        replicationRequestExecutor
      ).launch()
    }
    finally {
      log.info("Ended")
    }
  }
}

class ReplicatorTool(
  replicateDir: File,
  statusRepository: StatusRepositoryImpl,
  replicationStateRepository: ReplicationStateRepository,
  replicationRequestExecutor: ReplicationRequestExecutor
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
            case ReplicationResult.Ok =>
              val timestamp = replicationStateRepository.read(replicationId)
              log.info(s"OK ${timestamp.yyyymmddhhmmss}")
              statusRepository.writeReplicationStatus(replicationId)
              replicationId = replicationId.next
              /*
                We have successfully replicated the files for the current replication id. If we were in error mode
                before, we are no longer in error mode now.
               */
              inerror = false

            case ReplicationResult.NotFound =>
              /*
                The OpenStreetMap server told us that the files for the current replication id do not exist (yet). We
                assume that we have replicated all available files and wait for a longer time for new files to become
                available.
               */
              insync = true

            case ReplicationResult.Error =>
              /*
                An error occurred. We switch to error mode (with longer waiting time between retries), and we assume
                that we will no longer be in sync after we recover from the error mode.
               */
              inerror = true
              insync = false

            case ReplicationResult.End =>
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

  private def replicate(replicationId: ReplicationId): ReplicationResult.Value = {
    replicateChangesFile(replicationId) match {
      case ReplicationResult.Ok =>
        if (oper.isActive) {
          replicateStateFile(replicationId)
        }
        else {
          ReplicationResult.End
        }
      case result => result
    }
  }

  private def replicateChangesFile(replicationId: ReplicationId): ReplicationResult.Value = {
    replicationRequestExecutor.requestChangesFile(replicationId) match {
      case None => ReplicationResult.NotFound
      case Some(changesString) =>
        val file = new File(replicateDir + "/" + replicationId.name + ".osc.gz")
        file.getParentFile.mkdirs()
        GZipFile.write(file.getAbsolutePath, changesString)
        try {
          new OsmChangeReader(file.getAbsolutePath).read
          log.debug(file.getAbsolutePath + " integrity check OK")
          ReplicationResult.Ok
        }
        catch {
          case e: Exception =>
            log.error(file.getAbsolutePath + " integrity check 2 NOK", e)
            ReplicationResult.Error
        }
    }
  }

  private def replicateStateFile(replicationId: ReplicationId): ReplicationResult.Value = {
    replicationRequestExecutor.requestStateFile(replicationId) match {
      case None => ReplicationResult.NotFound
      case Some(stateString) =>
        replicationStateRepository.write(replicationId, stateString)
        ReplicationResult.Ok
    }
    ReplicationResult.Ok
  }

  private def sleep(seconds: Int): Unit = {
    log.debug("sleep " + seconds + "s")
    val end = System.currentTimeMillis() + (seconds * 1000)
    while (oper.isActive && System.currentTimeMillis() < end) {
      Thread.sleep(1000)
    }
  }
}
