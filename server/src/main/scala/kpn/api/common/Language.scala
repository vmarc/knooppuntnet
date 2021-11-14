package kpn.api.common

sealed trait Language

case object EN extends Language

case object NL extends Language

case object DE extends Language

case object FR extends Language

object Languages {
  val all: Seq[Language] = Seq(EN, NL, DE, FR)
}
