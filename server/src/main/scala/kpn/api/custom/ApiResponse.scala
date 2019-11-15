package kpn.api.custom

case class ApiResponse[T](situationOn: Option[Timestamp], version: Long, result: Option[T])
