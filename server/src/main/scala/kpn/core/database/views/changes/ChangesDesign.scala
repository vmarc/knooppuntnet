package kpn.core.database.views.changes

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object ChangesDesign extends Design {
  val views: Seq[View] = Seq(
    ChangesView
  )
}
