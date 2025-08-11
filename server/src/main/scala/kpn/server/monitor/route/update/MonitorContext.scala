package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorMessage
import kpn.server.monitor.domain.MonitorReference
import org.bson.types.ObjectId

import scala.compiletime.uninitialized

class MonitorContext {
  private var privateContext: MonitorUpdateContext = uninitialized

  def set(context: MonitorUpdateContext): Unit = {
    privateContext = context
  }

  def value: MonitorUpdateContext = {
    privateContext
  }

  def report(message: MonitorMessage): Unit = {
    privateContext.reporter.report(message)
  }

  def stepActive(stepId: String): Unit = {
    privateContext.reporter.stepActive(stepId)
  }

  def stepDone(stepId: String): Unit = {
    privateContext.reporter.stepDone(stepId)
  }

  def upsertRouteReference(reference: MonitorReference): Unit = {
    val refs = referencesWithout(reference)
    val updatedRefs = refs :+ reference
    set(value.copy(references = updatedRefs))
  }

  def deleteRouteReference(routeId: ObjectId, relationId: Option[Long]): Unit = {
    val refs = value.references.filterNot(ref => ref.routeId == routeId && ref.relationId == relationId)
    set(value.copy(references = refs))
  }

  def deleteRouteReference(reference: MonitorReference): Unit = {
    val refs = referencesWithout(reference)
    set(value.copy(references = refs))
  }

  def deleteRouteReferenceById(_id: ObjectId): Unit = {
    val refs = value.references.filterNot(ref => ref._id == _id)
    set(value.copy(references = refs))
  }

  private def referencesWithout(reference: MonitorReference): Seq[MonitorReference] = {
    value.references.filterNot(_._id == reference._id)
  }
}
