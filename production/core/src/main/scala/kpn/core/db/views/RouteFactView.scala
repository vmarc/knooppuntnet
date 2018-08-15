package kpn.core.db.views

import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

/*
 * Route facts per network
 *
 *
 */
object RouteFactView extends View {

  case class Row(country: String, networkType: String, factName: String, networkName: String, networkId: Long, routeName: Option[String], routeId: Option[Long])

  def convert(value: JsValue): Row = {
    val key = value.asJsObject.getFields("key").head
    key match {
      case JsArray(Vector(JsString(country), JsString(networkType), JsString(factName), JsString(networkName), JsNumber(networkId), JsString(routeName), JsNumber(routeId))) =>
        Row(country, networkType, factName, networkName, networkId.toLong, Some(routeName), Some(routeId.toLong))
      case JsArray(Vector(JsString(country), JsString(networkType), JsString(factName), JsString(networkName), JsNumber(networkId))) =>
        Row(country, networkType, factName, networkName, networkId.toLong, None, None)
    }
  }

  override val reduce: Option[String] = Some("_sum")
}
