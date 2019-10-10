package kpn.core.engine.changes.data

import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

import scala.collection.concurrent.TrieMap

object OrphanNodesData {

  def apply(): OrphanNodesData = new OrphanNodesData()

  object OrphanNodesDataFormat extends RootJsonFormat[OrphanNodesData] {
    def write(c: OrphanNodesData): JsValue = {
      JsArray(c.orphanNodeIds.keys.map(id => JsNumber(id)).toVector)
    }

    def read(value: JsValue): OrphanNodesData = {
      val data = new OrphanNodesData()
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

class OrphanNodesData {

  private var orphanNodeIds: scala.collection.concurrent.Map[Long, Unit] = TrieMap()

  def size: Int = orphanNodeIds.size

  def isEmpty: Boolean = orphanNodeIds.isEmpty

  def contains(nodeId: Long): Boolean = orphanNodeIds.contains(nodeId)

  def ids: Iterable[Long] = orphanNodeIds.keys

  def add(nodeId: Long): Unit = {
    orphanNodeIds += (nodeId -> Unit)
  }

  def delete(nodeId: Long): Unit = {
    orphanNodeIds -= nodeId
  }

  def delete(nodeIds: Iterable[Long]): Unit = {
    orphanNodeIds --= nodeIds
  }
}
