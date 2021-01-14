package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.action.ApiAction
import kpn.core.action.LogAction
import kpn.core.database.Database
import kpn.core.database.doc.ApiActionDoc
import kpn.core.database.doc.LogActionDoc
import kpn.core.database.views.metrics.FrontendMetricsView
import org.springframework.stereotype.Component

import scala.util.Random

@Component
class FrontendMetricsRepositoryImpl(frontendActionsDatabase: Database) extends FrontendMetricsRepository {

  private val random = new Random()

  override def saveApiAction(apiAction: ApiAction): Unit = {
    val id = s"api-${apiAction.timestamp.toId}-${random.nextInt(10000)}"
    frontendActionsDatabase.save(ApiActionDoc(id, apiAction))
  }

  override def saveLogAction(logAction: LogAction): Unit = {
    val id = s"log-${logAction.logfile}-${logAction.timestamp.toId}}"
    frontendActionsDatabase.save(LogActionDoc(id, logAction))
  }

  override def query(parameters: PeriodParameters, action: String, average: Boolean, stale: Boolean = true): Seq[NameValue] = {
    FrontendMetricsView.query(frontendActionsDatabase, parameters, action, average, stale)
  }

}
