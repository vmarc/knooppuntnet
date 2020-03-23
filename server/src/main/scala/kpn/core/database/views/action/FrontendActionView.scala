package kpn.core.database.views.action

import kpn.core.database.views.common.View

object FrontendActionView extends View {

  override def reduce: Option[String] = Some("_sum")

}
