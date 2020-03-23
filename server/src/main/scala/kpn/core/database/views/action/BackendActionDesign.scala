package kpn.core.database.views.action

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object BackendActionDesign extends Design {
  val views: Seq[View] = Seq(
    BackendActionView
  )
}
