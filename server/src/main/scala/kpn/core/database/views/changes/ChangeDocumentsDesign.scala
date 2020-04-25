package kpn.core.database.views.changes

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object ChangeDocumentsDesign extends Design {
  val views: Seq[View] = Seq(
    ChangeDocumentView
  )
}
