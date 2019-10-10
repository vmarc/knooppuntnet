package kpn.core.engine.changes

case class ElementChanges(
  creates: Seq[Long] = Seq.empty,
  updates: Seq[Long] = Seq.empty,
  deletes: Seq[Long] = Seq.empty
) {

  def nonEmpty: Boolean = creates.nonEmpty || updates.nonEmpty || deletes.nonEmpty

  def actionCount: Int = creates.size + updates.size + deletes.size
}
