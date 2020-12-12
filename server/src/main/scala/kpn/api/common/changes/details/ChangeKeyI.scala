package kpn.api.common.changes.details

case class ChangeKeyI(
  replicationNumber: Long,
  timestamp: String,
  changeSetId: Long,
  elementId: Long
) {
  def cleaned: ChangeKeyI = {
    val cleanedTimestamp = timestamp.replace('T', ' ').replace("Z", "")
    ChangeKeyI(
      replicationNumber,
      cleanedTimestamp,
      changeSetId,
      elementId
    )
  }
}