package kpn.server.analyzer.engine.changes.network.update

import java.util.concurrent.CompletableFuture

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait NetworkUpdateProcessor {
  def process(context: ChangeSetContext, networkId: Long): CompletableFuture[ChangeSetChanges]
}
