package kpn.api.common.diff

case class TagDetail(action: TagDetailType, key: String, valueBefore: Option[String], valueAfter: Option[String]) {

  def name: String = action.toString.toLowerCase

  def sortKey: String = action.name + " " + key

}
