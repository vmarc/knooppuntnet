package kpn.core.database.views.analyzer

import kpn.core.app.NetworkIntegrityInfo
import kpn.core.database.views.common.View
import kpn.shared.NetworkIntegrityCheckFailed
import kpn.shared.NodeIntegrityCheck
import spray.json.DeserializationException
import spray.json.JsArray
import spray.json.JsBoolean
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

object FactView extends View {

  case class FactViewKey(
    country: String,
    networkType: String,
    fact: String,
    networkName: String,
    networkId: Long
  )

  def convert(rowValue: JsValue): FactViewKey = {
    val row = toRow(rowValue)
    row.key match {
      case JsArray(Vector(JsString(country), JsString(networkType), JsString(fact), JsString(networkName), JsNumber(networkId))) =>
        FactViewKey(country, networkType, fact, networkName, networkId.toLong)
      case _ => throw DeserializationException("expected array")
    }
  }

  def integrityCheckConvert(rowValue: JsValue): NetworkIntegrityInfo = {

    val row = toRow(rowValue)

    row.key match {
      case JsArray(Vector(JsString(country), JsString(networkType), JsString(fact), JsString(networkName), JsNumber(networkId))) =>
        row.value.asJsObject.getFields("count", "checks") match {
          case Seq(JsNumber(count), JsArray(checkValues)) =>
            val checks = checkValues.map { check =>
              check.asJsObject.getFields("expected", "failed", "nodeName", "actual", "nodeId") match {
                case Seq(JsNumber(expected), JsBoolean(failed), JsString(nodeName), JsNumber(actual), JsNumber(nodeId)) =>
                  NodeIntegrityCheck(nodeName, nodeId.toLong, actual.toInt, expected.toInt, failed)
                case _ => throw DeserializationException("NodeIntegrityCheck fields expected")
              }
            }

            val detail = NetworkIntegrityCheckFailed(count.toInt, checks)
            NetworkIntegrityInfo(networkId.toLong, networkName, detail)

          case _ => throw DeserializationException("row value expected")
        }

      case _ => throw DeserializationException("row key expected")
    }
  }
}
