package kpn.client.filter

class Parameters(parameters: Map[String, Seq[String]]) {

  def strings(name: String): Seq[String] = parameters.getOrElse(name, Seq())

  def boolean(name: String): Option[Boolean] = {
    parameters.get(name) match {
      case Some(Seq("yes")) => Some(true)
      case Some(Seq("no")) => Some(false)
      case _ => None
    }
  }

  def int(name: String): Int = parameters.get(name) match {
    case Some(Seq(value)) =>
      value.filter(_.isDigit) match  {
        case x if x.isEmpty => 1
        case digits => digits.toInt
      }
    case _ => 1
  }

  def timestamp(name: String): TimeFilterKind.Value = {
    val result = parameters.get(name) match {
      case Some(Seq("all")) => TimeFilterKind.ALL
      case Some(Seq("lastWeek")) => TimeFilterKind.LAST_WEEK
      case Some(Seq("lastMonth")) => TimeFilterKind.LAST_MONTH
      case Some(Seq("lastYear")) => TimeFilterKind.LAST_YEAR
      case Some(Seq("older")) => TimeFilterKind.OLDER
      case _ => TimeFilterKind.ALL
    }

    println("TIMESTAMP PARAMETER=" + result + ", name=" + name + ", para=" + parameters.get(name) )

    result
  }
}
