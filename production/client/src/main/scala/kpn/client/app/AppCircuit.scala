// TODO migrate to Angular
package kpn.client.app

import autowire._
import diode._
import diode.data._
import diode.react.ReactConnector
import diode.util._
import kpn.client.services.AjaxClient
import kpn.shared.Api
import kpn.shared.ApiResponse
import kpn.shared.statistics.Statistics

import scala.concurrent.duration.DurationInt
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

// need to keep these imports, although IntelliJ thinks they are not used
import boopickle.DefaultBasic._
import kpn.shared.KpnPicklers._

case class RootModel(statistics: Pot[ApiResponse[Statistics]])

case class GetStatistics(potResult: Pot[ApiResponse[Statistics]] = Empty) extends PotAction[ApiResponse[Statistics], GetStatistics] {
  override def next(value: Pot[ApiResponse[Statistics]]) = GetStatistics(value)
}

class StatisticsHandler[M](modelRW: ModelRW[M, Pot[ApiResponse[Statistics]]]) extends ActionHandler(modelRW) {

  implicit val runner: RunAfter = new RunAfterJS

  override def handle: PartialFunction[Any, ActionResult[M]] = {
    case action: GetStatistics =>
      val getStatistics = action.effect(AjaxClient[Api].overview().call())(identity _)
      action.handleWith(this, getStatistics)(PotAction.handler(500.millis))
  }
}

object AppCircuit extends Circuit[RootModel] with ReactConnector[RootModel] {

  override protected def initialModel = RootModel(Empty)

  override protected val actionHandler: AppCircuit.HandlerFunction = composeHandlers(
    new StatisticsHandler(
      zoomRW(_.statistics) { (m, v) =>
        m.copy(statistics = v)
      }
    )
  )
}
