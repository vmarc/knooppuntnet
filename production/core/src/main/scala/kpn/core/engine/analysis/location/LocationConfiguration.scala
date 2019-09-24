package kpn.core.engine.analysis.location

object LocationConfiguration {

  val nl = LocationConfiguration("nl", Seq(3, 4, 8))
  val be = LocationConfiguration("be", Seq(2, 4, 6, 7, 8))
  val de = LocationConfiguration("de", Seq(2, 4, 5, 6))
  val fr = LocationConfiguration("fr", Seq(3, 4, 7))

  val countries: Seq[LocationConfiguration] = Seq(nl, be, de, fr)
}

case class LocationConfiguration(country: String, levels: Seq[Int])
