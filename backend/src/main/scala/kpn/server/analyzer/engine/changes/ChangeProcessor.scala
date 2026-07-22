package kpn.server.analyzer.engine.changes

trait ChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetContext
}
