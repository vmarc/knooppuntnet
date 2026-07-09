package kpn.core.tools.operation

import kpn.core.replicate.ReplicationStateRepository
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.support.Stop

import scala.sys.process.Process

class OperationActions {

  private val dirs = Dirs()
  private val systemStatus = new SystemStatus(
    new ProcessReporter(),
    new StatusRepository(dirs),
    new ReplicationStateRepository(dirs.replicate)
  )

  def status(web: Boolean): String = {
    systemStatus.status(web)
  }

  def startMainDispatcher(): String = {
    Process(s"${Dirs.root}/scripts/dispatcher.sh").!!
  }

  def startAreasDispatcher(): String = {
    Process(s"${Dirs.root}/scripts/areas_dispatcher.sh").!!
  }

  def startReplicator(): String = {
    Process(s"${Dirs.root}/scripts/replicator.sh").!!
  }

  def startUpdater(): String = {
    Process(s"${Dirs.root}/scripts/updater.sh").!!
  }

  def startAnalyzer1(): String = {
    Process(s"${Dirs.root}/scripts/analyzer-1.sh").!!
  }

  def startAnalyzer2(): String = {
    Process(s"${Dirs.root}/scripts/analyzer-2.sh").!!
  }

  def startAnalyzer3(): String = {
    Process(s"${Dirs.root}/scripts/analyzer-3.sh").!!
  }

  def startServer(): String = {
    Process(s"${Dirs.root}/scripts/server.sh").!!
  }

  def startServerHistory(): String = {
    Process(s"${Dirs.root}/scripts/server-history.sh").!!
  }

  def startChangeSetInfoTool(): String = {
    Process(s"${Dirs.root}/scripts/change-set-info-tool.sh").!!
  }

  def startChangeSetInfoTool2(): String = {
    Process(s"${Dirs.root}/scripts/change-set-info-tool-2.sh").!!
  }

  def stopMainDispatcher(): String = {
    Process(s"${Dirs.root}/overpass/bin/dispatcher --terminate --osm-base").!!
  }

  def stopAreasDispatcher(): String = {
    Process(s"${Dirs.root}/overpass/bin/dispatcher --terminate --areas").!!
  }

  def stopReplicator(): String = {
    new Stop().stop("5102")
    ""
  }

  def stopUpdater(): String = {
    new Stop().stop("5103")
    ""
  }

  def stopAnalyzer1(): String = {
    new Stop().stop("5201")
    ""
  }

  def stopAnalyzer2(): String = {
    new Stop().stop("5202")
    ""
  }

  def stopAnalyzer3(): String = {
    new Stop().stop("5203")
    ""
  }

  def stopServer(): String = {
    new Stop().stopServer("5101")
    ""
  }

  def stopServerHistory(): String = {
    new Stop().stopServer("5111")
    ""
  }

  def stopChangeSetInfoTool(): String = {
    new Stop().stop("5204")
    ""
  }

  def stopChangeSetInfoTool2(): String = {
    new Stop().stop("5244")
    ""
  }
}
