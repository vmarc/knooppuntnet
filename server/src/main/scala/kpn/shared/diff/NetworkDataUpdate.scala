package kpn.shared.diff

case class NetworkDataUpdate(
  before: NetworkData,
  after: NetworkData
) {

  def happy: Boolean = false // diffs.happy

  def isNewVersion: Boolean = before.relation.version != after.relation.version

  def investigate: Boolean =  false // diffs.investigate
}
