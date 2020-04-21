package kpn.core.tools.operation

import kpn.core.replicate.ReplicationStateRepositoryImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.core.tools.support.Stop

import scala.sys.process.Process

class OperationActions {

  private val dirs = Dirs()
  private val systemStatus = new SystemStatus(
    new ProcessReporterImpl(),
    new StatusRepositoryImpl(dirs),
    new ReplicationStateRepositoryImpl(dirs.replicate)
  )

  def status(): String = {
    systemStatus.status()
  }

  def startMainDispatcher(): String = {
    Process("/kpn/scripts/dispatcher.sh").!!
  }

  def startAreasDispatcher(): String = {
    Process("/kpn/scripts/areas_dispatcher.sh").!!
  }

  def startReplicator(): String = {
    Process("/kpn/scripts/replicator.sh").!!
  }

  def startUpdater(): String = {
    Process("/kpn/scripts/updater.sh").!!
  }

  def startAnalyzer1(): String = {
    Process("/kpn/scripts/analyzer-1.sh").!!
  }

  def startAnalyzer2(): String = {
    Process("/kpn/scripts/analyzer-2.sh").!!
  }

  def startAnalyzer3(): String = {
    Process("/kpn/scripts/analyzer-3.sh").!!
  }

  def startServer(): String = {
    Process("/kpn/scripts/server.sh").!!
  }

  def startChangeSetInfoTool(): String = {
    Process("/kpn/scripts/change-set-info-tool.sh").!!
  }

  def startChangeSetInfoTool2(): String = {
    Process("/kpn/scripts/change-set-info-tool-2.sh").!!
  }

  def stopMainDispatcher(): String = {
    Process("/kpn/overpass/bin/dispatcher --terminate --osm-base").!!
  }

  def stopAreasDispatcher(): String = {
    Process("/kpn/overpass/bin/dispatcher --terminate --areas").!!
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

  def stopChangeSetInfoTool(): String = {
    new Stop().stop("5204")
    ""
  }

  def stopChangeSetInfoTool2(): String = {
    new Stop().stop("5244")
    ""
  }

}
