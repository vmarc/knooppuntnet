package kpn.api.common.common

case class UserSession(
  _id: String,
  intervalSeconds: Long,
  createdMillis: Long,
  accessedMillis: Long,
  expireAt: String,
  principal: Option[String]
)
