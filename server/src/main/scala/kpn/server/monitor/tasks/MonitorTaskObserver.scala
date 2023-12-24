package kpn.server.monitor.tasks

import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorTask
import org.mongodb.scala.model.changestream.ChangeStreamDocument
import org.mongodb.scala.Observer
import org.mongodb.scala.Subscription

import java.util.concurrent.CountDownLatch

case class MonitorTaskObserver() extends Observer[ChangeStreamDocument[MonitorTask]] {

  private val latch = new CountDownLatch(1)
  private var savedSubscription: Option[Subscription] = None
  private val log = Log(classOf[MonitorTaskObserver])

  override def onSubscribe(subscription: Subscription): Unit = {
    subscription.request(1)
    savedSubscription = Some(subscription)
  }

  override def onNext(changeDocument: ChangeStreamDocument[MonitorTask]): Unit = {
  }

  override def onError(throwable: Throwable): Unit = {
    log.error(s"Something went wrong while waiting for new task: '$throwable")
    cancelSubscription()
  }

  override def onComplete(): Unit = {
    cancelSubscription()
  }

  def cancel(): Unit = {
    cancelSubscription()
  }

  def await(): Unit = {
    latch.await()
  }

  def isSubscribed: Boolean = {
    savedSubscription match {
      case None => false
      case Some(subscription) => !subscription.isUnsubscribed
    }
  }

  private def cancelSubscription(): Unit = {
    savedSubscription match {
      case Some(subscription) => subscription.cancel()
      case None =>
    }
    latch.countDown()
  }
}
