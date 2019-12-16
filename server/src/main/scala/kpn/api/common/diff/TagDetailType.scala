package kpn.api.common.diff

object TagDetailType {
  val Same: TagDetailType = TagDetailType("Same")
  val Delete: TagDetailType = TagDetailType("Delete")
  val Update: TagDetailType = TagDetailType("Update")
  val Add: TagDetailType = TagDetailType("Add")
}

case class TagDetailType(name: String)
