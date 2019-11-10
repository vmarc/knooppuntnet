package kpn.api.common

import kpn.api.common.common.Ref

case class NetworkFact(
  name: String,
  elementType: Option[String] = None,
  elementIds: Option[Seq[Long]] = None,
  elements: Option[Seq[Ref]] = None,
  checks: Option[Seq[Check]] = None
)
