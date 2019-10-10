package kpn.shared.diff

object TagDetailType {
  val Same = TagDetailType("Same")
  val Delete = TagDetailType("Delete")
  val Update = TagDetailType("Update")
  val Add = TagDetailType("Add")
}

case class TagDetailType(name: String)
