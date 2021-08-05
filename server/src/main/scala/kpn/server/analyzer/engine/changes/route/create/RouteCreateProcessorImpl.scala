package kpn.server.analyzer.engine.changes.route.create

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import org.springframework.stereotype.Component

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

@Component
class RouteCreateProcessorImpl(
  implicit val analysisExecutionContext: ExecutionContext
) extends RouteCreateProcessor {

  private val log = Log(classOf[RouteCreateProcessorImpl])

  override def process(context: ChangeSetContext, routeId: Long): Future[ChangeSetChanges] = {
    Future(
      Log.context(s"routeId=$routeId") {

        //  try {
        //    networkLoader.load(Some(context.timestampAfter), networkId) match {
        //      case None =>
        //
        //        log.error(
        //          s"Processing network create from changeset ${context.replicationId.name}\n" +
        //            s"Could not load network with id $networkId at ${context.timestampAfter.yyyymmddhhmmss}.\n" +
        //            "Continue processing changeset without this network."
        //        )
        //        ChangeSetChanges()
        //
        //      case Some(loadedNetwork) =>
        //        watchedProcessor.process(context, loadedNetwork)
        //    }
        //  }
        //  catch {
        //    case e: Throwable =>
        //      val message = s"Exception while processing network create (networkId=$networkId) at ${context.timestampAfter.yyyymmddhhmmss} in changeset ${context.replicationId.name}."
        //      log.error(message, e)
        //      throw e
        //  }
        //
        //  //        val changeSetChanges = doProcess(context, networkId)
        //  //        (s"${changeSetChanges.size} change(s)", changeSetChanges)
        null
      }
    )
  }
}
