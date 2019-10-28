package kpn.core.database.views.analyzer

import kpn.core.database.views.common.View
import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

/*
 * Facts per network
 */
object FactsPerNetworkView extends View {

  case class Row(country: String, networkType: String, factName: String, networkName: String, networkId: Long, referrerName: Option[String], referrerId: Option[Long])

  def convert(value: JsValue): Row = {
    val key = value.asJsObject.getFields("key").head
    key match {
      case JsArray(Vector(JsString(country), JsString(networkType), JsString(factName), JsString(networkName), JsNumber(networkId), JsString(referrerName), JsNumber(referrerId))) =>
        Row(country, networkType, factName, networkName, networkId.toLong, Some(referrerName), Some(referrerId.toLong))
      case JsArray(Vector(JsString(country), JsString(networkType), JsString(factName), JsString(networkName), JsNumber(networkId))) =>
        Row(country, networkType, factName, networkName, networkId.toLong, None, None)
    }
  }

  override val reduce: Option[String] = Some("_sum")
}
