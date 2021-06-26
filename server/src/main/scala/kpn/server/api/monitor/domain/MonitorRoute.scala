package kpn.server.api.monitor.domain

import kpn.api.base.WithId
import kpn.api.common.DE
import kpn.api.common.EN
import kpn.api.common.FR
import kpn.api.common.Language
import kpn.api.common.NL

object MonitorRoute {

  // for mongodb migration only
  def apply(
    _id: Long,
    groupName: String,
    name: String,
    nameNl: Option[String],
    nameEn: Option[String],
    nameDe: Option[String],
    nameFr: Option[String],
    ref: Option[String],
    description: Option[String],
    operator: Option[String],
    website: Option[String]
  ): MonitorRoute = {
    MonitorRoute(
      _id,
      _id,
      groupName,
      name,
      nameNl,
      nameEn,
      nameDe,
      nameFr,
      ref,
      description,
      operator,
      website
    )
  }
}

case class MonitorRoute(
  _id: Long, // == id
  id: Long,
  groupName: String,
  name: String,
  nameNl: Option[String],
  nameEn: Option[String],
  nameDe: Option[String],
  nameFr: Option[String],
  ref: Option[String],
  description: Option[String],
  operator: Option[String],
  website: Option[String]
) extends WithId {

  // for mongodb migration only
  def toMongo: MonitorRoute = {
    copy(_id = id)
  }

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
