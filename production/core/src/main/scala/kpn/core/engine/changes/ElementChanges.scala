package kpn.core.engine.changes

case class ElementChanges(creates: Seq[Long] = Seq(), updates: Seq[Long] = Seq(), deletes: Seq[Long] = Seq()) {
  def nonEmpty: Boolean = creates.nonEmpty || updates.nonEmpty || deletes.nonEmpty
  def actionCount: Int = creates.size + updates.size + deletes.size
}
