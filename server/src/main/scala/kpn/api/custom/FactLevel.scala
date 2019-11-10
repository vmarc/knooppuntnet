package kpn.api.custom

object FactLevel {
  val ERROR = FactLevel("error")
  val INFO = FactLevel("info")
  val OTHER = FactLevel("other")
}

case class FactLevel(name: String)
