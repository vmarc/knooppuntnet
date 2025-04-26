package kpn.api.common.diff

object TagDiff {
  def same(key: String, value: String): TagDiff = {
    TagDiff(TagDiffType.same, key, Some(value), Some(value))
  }

  def add(key: String, value: String): TagDiff = {
    TagDiff(TagDiffType.add, key, None, Some(value))
  }

  def delete(key: String, value: String): TagDiff = {
    TagDiff(TagDiffType.delete, key, Some(value), None)
  }

  def update(key: String, valueBefore: String, valueAfter: String): TagDiff = {
    TagDiff(TagDiffType.update, key, Some(valueBefore), Some(valueAfter))
  }
}

case class TagDiff(action: TagDiffType, key: String, valueBefore: Option[String], valueAfter: Option[String]) {

  def name: String = action.toString.toLowerCase

  def sortKey: String = s"${action.entryName} $key"
}
