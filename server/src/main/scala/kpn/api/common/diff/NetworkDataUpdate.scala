package kpn.api.common.diff

case class NetworkDataUpdate(
  before: Option[NetworkData],
  after: Option[NetworkData]
)
