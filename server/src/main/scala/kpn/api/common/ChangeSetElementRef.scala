package kpn.api.common

import kpn.api.common.common.Ref

case class ChangeSetElementRef(
  id: Long,
  name: String,
  happy: Boolean,
  investigate: Boolean
) {

  def toRef: Ref = Ref(id, name)

}
