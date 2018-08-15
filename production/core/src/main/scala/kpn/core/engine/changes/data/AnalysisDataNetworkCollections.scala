package kpn.core.engine.changes.data

import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

import scala.collection.concurrent.TrieMap

object AnalysisDataNetworkCollections {

  def apply(): AnalysisDataNetworkCollections = new AnalysisDataNetworkCollections()

  object AnalysisDataNetworkCollectionsFormat extends RootJsonFormat[AnalysisDataNetworkCollections] {
    def write(c: AnalysisDataNetworkCollections): JsValue = {
      JsArray(c.networkCollections.keys.map(id => JsNumber(id)).toVector)
    }

    def read(value: JsValue): AnalysisDataNetworkCollections = {
      val data = new AnalysisDataNetworkCollections()
      value match {
        case JsArray(values) =>
          values.foreach {
            case JsNumber(id) => data.add(id.toLong)
            case _ => deserializationError("number expected")
          }
        case _ => deserializationError("array expected")
      }
      data
    }
  }

}

class AnalysisDataNetworkCollections {

  private var networkCollections: scala.collection.concurrent.Map[Long, Unit] = TrieMap()

  def size: Int = networkCollections.size

  def isEmpty: Boolean = networkCollections.isEmpty

  def ids: Iterable[Long] = networkCollections.keys

  def add(id: Long): Unit = {
    networkCollections += (id -> Unit)
  }

  def contains(id: Long): Boolean = networkCollections.contains(id)

}
