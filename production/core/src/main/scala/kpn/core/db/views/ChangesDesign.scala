package kpn.core.db.views

object ChangesDesign extends Design {
  val views: Seq[View] = Seq(
    DocumentView,
    ChangesView
  )
}
