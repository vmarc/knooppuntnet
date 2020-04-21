package kpn.server.analyzer.engine.changes.network

import java.util.concurrent.CompletableFuture.allOf

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessor
import kpn.server.analyzer.engine.changes.network.delete.NetworkDeleteProcessor
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessor
import org.springframework.stereotype.Component

@Component
class NetworkChangeProcessorImpl(
  changeAnalyzer: NetworkChangeAnalyzer,
  createProcessor: NetworkCreateProcessor,
  updateProcessor: NetworkUpdateProcessor,
  deleteProcessor: NetworkDeleteProcessor
) extends NetworkChangeProcessor {

  private val log = Log(classOf[NetworkChangeProcessorImpl])

  def process(context: ChangeSetContext): ChangeSetChanges = {

    log.debugElapsed {

      val networkChanges = changeAnalyzer.analyze(context.changeSet)

      val createFutures = networkChanges.creates.map(id => createProcessor.process(context, id))
      val updateFutures = networkChanges.updates.map(id => updateProcessor.process(context, id))
      val deleteFutures = networkChanges.deletes.map(id => deleteProcessor.process(context, id))

      val futures = createFutures ++ updateFutures ++ deleteFutures

      allOf(futures: _*).join()

      val changes = futures.map(s => s.get())
      val changeSetChanges = merge(changes: _*)
      val message = s"actions=${networkChanges.actionCount}, creates=${createFutures.size}, updates=${updateFutures.size}, deletes=${deleteFutures.size}"
      (message, changeSetChanges)
    }
  }
}
