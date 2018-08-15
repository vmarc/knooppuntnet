package kpn.core.engine.changes

import kpn.core.changes.ElementIds
import kpn.core.db.json.JsonFormats
import spray.json.JsObject
import spray.json.JsValue
import spray.json.RootJsonFormat

import scala.collection.concurrent.TrieMap

object ElementIdMap {

  def apply(): ElementIdMap = new ElementIdMap()

  object ElementIdMapFormat extends RootJsonFormat[ElementIdMap] {
    def write(c: ElementIdMap): JsValue = {
      val fields = c.elementMap.map { case (id, elementIds) =>
        id.toString -> JsonFormats.elementIdsFormat.write(elementIds)
      }.toMap
      JsObject(fields)
    }

    def read(value: JsValue): ElementIdMap = {
      val elementIdMap = ElementIdMap()
      value.asJsObject.fields.foreach { case (id, elementIds) =>
        elementIdMap.add(id.toLong, JsonFormats.elementIdsFormat.read(elementIds))
      }
      elementIdMap
    }
  }

}

class ElementIdMap {

  private var elementMap: scala.collection.concurrent.Map[Long, ElementIds] = TrieMap()

  def size: Int = elementMap.size

  def isEmpty: Boolean = elementMap.isEmpty

  def ids: Iterable[Long] = elementMap.keySet

  def get(key: Long): Option[ElementIds] = elementMap.get(key)

  def add(id: Long, elementIds: ElementIds): Unit = {
    elementMap += (id -> elementIds)
  }

  def delete(id: Long): Unit = {
    elementMap -= id
  }

  def delete(ids: Iterable[Long]): Unit = {
    elementMap --= ids
  }

  def contains(id: Long): Boolean = {
    elementMap.contains(id)
  }

  def isReferencingNode(nodeId: Long): Boolean = {
    elementMap.values.exists(_.nodeIds.contains(nodeId))
  }

  def isReferencingRelation(relationId: Long): Boolean = {
    elementMap.values.exists(_.relationIds.contains(relationId))
  }

  /*
   * Finds the ids of all the elements that contain at least 1 of given elements.
   */
  def referencedBy(elementIds: ElementIds): Iterable[Long] = {
    elementMap.filter { case (key, value) =>
      value.relationIds.contains(key) ||
        elementIds.relationIds.exists(value.relationIds.contains) ||
        elementIds.wayIds.exists(value.wayIds.contains) ||
        elementIds.nodeIds.exists(value.nodeIds.contains)
    }.keySet
  }
}
