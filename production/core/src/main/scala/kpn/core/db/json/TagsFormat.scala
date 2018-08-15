package kpn.core.db.json

import kpn.shared.data.Tag
import kpn.shared.data.Tags
import spray.json.JsArray
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

object TagsFormat extends RootJsonFormat[Tags] {

  def write(tags: Tags): JsValue = {
    val keysAndValues = tags.tags.map { tag =>
      JsArray(
        JsString(tag.key),
        JsString(tag.value)
      )
    }
    JsArray(keysAndValues: _*)
  }

  def read(jsonValue: JsValue): Tags = {
    jsonValue match {
      case JsArray(keysAndValues) =>
        Tags(
          keysAndValues.map { case JsArray(Vector(JsString(key), JsString(value))) =>
            Tag(key, value)
          }
        )
      case _ => deserializationError("Unexpected object")
    }
  }
}
