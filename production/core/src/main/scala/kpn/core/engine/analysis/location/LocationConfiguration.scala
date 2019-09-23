package kpn.core.engine.analysis.location

object LocationConfiguration {

  val nl = LocationConfiguration("nl", Seq("AL3", "AL4", "AL8"))
  val be = LocationConfiguration("be", Seq("AL2", /*"AL4",*/ "AL6", "AL7", "AL8"))
  val de = LocationConfiguration("de", Seq("AL2", "AL4", "AL5", "AL7"))
  val fr = LocationConfiguration("fr", Seq("AL3", "AL4", "AL7"))

  val countries: Seq[LocationConfiguration] = Seq(nl, be, de, fr)
}

case class LocationConfiguration(country: String, levels: Seq[String])
