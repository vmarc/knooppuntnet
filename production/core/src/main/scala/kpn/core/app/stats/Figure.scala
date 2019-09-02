package kpn.core.app.stats

import kpn.core.util.Formatter.number
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.statistics.CountryStatistic
import kpn.shared.statistics.Statistic

case class Figure(
  name: String,
  nlRwn: Int = 0,
  nlRcn: Int = 0,
  nlRhn: Int = 0,
  nlRmn: Int = 0,
  nlRpn: Int = 0,
  nlRin: Int = 0,
  beRwn: Int = 0,
  beRcn: Int = 0,
  beRhn: Int = 0,
  beRmn: Int = 0,
  beRpn: Int = 0,
  beRin: Int = 0,
  deRwn: Int = 0,
  deRcn: Int = 0,
  deRhn: Int = 0,
  deRmn: Int = 0,
  deRpn: Int = 0,
  deRin: Int = 0
) {

  def total: Int = nlRwn + nlRcn + nlRhn + nlRmn + nlRpn + nlRin +
    beRwn + beRcn + beRhn + beRmn + beRpn + beRin +
    deRwn + deRcn + deRhn + deRmn + deRpn + deRin

  def value(subset: Subset): Int = {
    subset.country match {
      case Country.nl =>
        subset.networkType match {
          case NetworkType.hiking => nlRwn
          case NetworkType.bicycle => nlRcn
          case NetworkType.horseRiding => nlRhn
          case NetworkType.motorboat => nlRmn
          case NetworkType.canoe => nlRpn
          case NetworkType.inlineSkates => nlRin
        }
      case Country.be =>
        subset.networkType match {
          case NetworkType.hiking => beRwn
          case NetworkType.bicycle => beRcn
          case NetworkType.horseRiding => beRhn
          case NetworkType.motorboat => beRmn
          case NetworkType.canoe => beRpn
          case NetworkType.inlineSkates => beRin
        }
      case Country.de =>
        subset.networkType match {
          case NetworkType.hiking => deRwn
          case NetworkType.bicycle => deRcn
          case NetworkType.horseRiding => deRhn
          case NetworkType.motorboat => deRmn
          case NetworkType.canoe => deRpn
          case NetworkType.inlineSkates => deRin
        }
    }
  }

  def toStatistic: Statistic = {
    Statistic(
      number(total),
      CountryStatistic(number(nlRwn), number(nlRcn), number(nlRhn), number(nlRmn), number(nlRpn), number(nlRin)),
      CountryStatistic(number(beRwn), number(beRcn), number(beRhn), number(beRmn), number(beRpn), number(beRin)),
      CountryStatistic(number(deRwn), number(deRcn), number(deRhn), number(deRmn), number(deRpn), number(deRin))
    )
  }
}
