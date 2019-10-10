package kpn.shared

import kpn.shared.common.Ref

case class ChangeSetElementRef(
  id: Long,
  name: String,
  happy: Boolean,
  investigate: Boolean
) {

  def toRef: Ref = Ref(id, name)

}
