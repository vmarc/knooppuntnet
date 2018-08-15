package kpn.core.db.views

import spray.json.DeserializationException
import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsString
import spray.json.JsValue

object ReferenceView extends View {

  case class Row(
    referencedType: String,
    referencedId: Long,
    referrerType: String,
    referrerId: Long,
    referrerNetworkType: String,
    referrerName: String,
    referenceCount: Int,
    connection: Boolean
  )

  case class Level2Row(
    referencedType: String,
    referencedId: Long,
    referenceCount: Int
  )

  def convert(rowValue: JsValue): Row = {
    val row = toRow(rowValue)
    row.key match {
      case JsArray(Vector(JsString(referencedType), JsNumber(referencedId), JsString(referrerType), JsNumber(referrerId))) =>
        row.value match {
          case JsArray(Vector(JsNumber(referenceCount), JsString(referrerNetworkType), JsString(referrerName), JsString(connection))) =>
            Row(referencedType, referencedId.toLong, referrerType, referrerId.toLong, referrerNetworkType, referrerName, referenceCount.toInt, connection == "true")
          case _ =>
            throw DeserializationException("value structure expected")
        }
      case _ =>
        throw DeserializationException("key structure expected")
    }
  }

  def convertLevel2(rowValue: JsValue): Level2Row = {
    val row = toRow(rowValue)
    row.key match {
      case JsArray(Vector(JsString(referencedType), JsNumber(referencedId))) =>
        row.value match {
          case JsNumber(referenceCount) =>
            Level2Row(referencedType, referencedId.toLong, referenceCount.toInt)
          case _ => throw DeserializationException("int value expected")
        }
      case _ => throw DeserializationException("key structure expected")
    }
  }
}
