package kpn.server.api.monitor.domain

import kpn.api.base.WithId
import kpn.api.common.DE
import kpn.api.common.EN
import kpn.api.common.FR
import kpn.api.common.Language
import kpn.api.common.NL

case class MonitorRoute(
  _id: Long, // routeId
  groupName: String,
  name: String,
  nameNl: Option[String] = None,
  nameEn: Option[String] = None,
  nameDe: Option[String] = None,
  nameFr: Option[String] = None,
  ref: Option[String] = None,
  description: Option[String] = None,
  operator: Option[String] = None,
  website: Option[String] = None
) extends WithId {

  def routeId: Long = _id

  def translatedName(language: Language): String = {
    language match {
      case NL => nameNl.getOrElse(name)
      case EN => nameEn.getOrElse(name)
      case FR => nameFr.getOrElse(name)
      case DE => nameDe.getOrElse(name)
      case _ => name
    }
  }
}
