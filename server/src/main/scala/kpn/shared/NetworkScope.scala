package kpn.shared

object NetworkScope {

  val local: NetworkScope = NetworkScope("local", "l")
  val regional: NetworkScope = NetworkScope("regional", "r")
  val national: NetworkScope = NetworkScope("national", "n")
  val international: NetworkScope = NetworkScope("international", "i")

  val all: Seq[NetworkScope] = Seq(local, regional, national, international)
}

case class NetworkScope(name: String, letter: String)
