package kpn.api.custom

object FactLevel {
  val ERROR: FactLevel = FactLevel("error")
  val INFO: FactLevel = FactLevel("info")
  val OTHER: FactLevel = FactLevel("other")
}

case class FactLevel(name: String)
