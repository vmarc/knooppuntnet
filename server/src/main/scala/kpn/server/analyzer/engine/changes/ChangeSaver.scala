package kpn.server.analyzer.engine.changes

trait ChangeSaver {
  def save(context: ChangeSetContext): Unit
}
