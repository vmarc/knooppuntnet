package kpn.server.repository

import kpn.core.action.ApiAction
import kpn.core.database.Database
import kpn.core.database.doc.ApiActionDoc
import org.springframework.stereotype.Component

import scala.util.Random

@Component
class FrontendMetricsRepositoryImpl(frontendActionsDatabase: Database) extends FrontendMetricsRepository {

  private val random = new Random()

  def saveApiAction(apiAction: ApiAction): Unit = {
    val id = s"api-${apiAction.timestamp.toId}-${random.nextInt(10000)}"
    frontendActionsDatabase.save(ApiActionDoc(id, apiAction))
  }

}
