package kpn.core.engine.changes.network

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.core.engine.changes.network.create.NetworkCreateProcessor
import kpn.core.engine.changes.network.delete.NetworkDeleteProcessor
import kpn.core.engine.changes.network.update.NetworkUpdateProcessor
import kpn.core.util.Log

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

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

      val creates = awaitResult(createFutures)
      val updates = awaitResult(updateFutures)
      val deletes = awaitResult(deleteFutures)

      val changes = merge((creates ++ updates ++ deletes): _*)

      val message = s"actions=${networkChanges.actionCount}, creates=${creates.size}, updates=${updates.size}, deletes=${deletes.size}"
      (message, changes)
    }
  }

  private def awaitResult(futures: Seq[Future[ChangeSetChanges]]): Seq[ChangeSetChanges] = {
    Await.result(Future.sequence(futures), Duration.Inf)
  }
}
