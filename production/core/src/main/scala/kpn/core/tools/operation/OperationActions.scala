package kpn.core.tools.operation

import kpn.core.replicate.ReplicationStateRepositoryImpl
import kpn.core.tools.Stop
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepositoryImpl

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

  def stopMainDispatcher(): String = {
    Process("/kpn/overpass/bin/dispatcher --terminate --osm-base").!!
  }

  def stopAreasDispatcher(): String = {
    Process("/kpn/overpass/bin/dispatcher --terminate --areas").!!
  }

  def stopReplicator(): String = {
    new Stop().stop("5551")
    ""
  }

  def stopUpdater(): String = {
    new Stop().stop("5552")
    ""
  }

  def stopAnalyzer1(): String = {
    new Stop().stop("5553")
    ""
  }

  def stopAnalyzer2(): String = {
    new Stop().stop("5554")
    ""
  }

  def stopAnalyzer3(): String = {
    new Stop().stop("5555")
    ""
  }
}
