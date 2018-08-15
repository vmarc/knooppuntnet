package kpn.shared

case class ApiResponse[T](situationOn: Option[Timestamp], version: Int, result: Option[T]) {
}
