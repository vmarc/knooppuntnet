package kpn.core.history

import kpn.api.common.data.Tagable
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType

object NodeTagDiffAnalyzer {

  private val prefixes = NetworkScope.all.flatMap { networkScope =>
    NetworkType.all.map { networkType =>
      networkScope.letter + networkType.letter
    }
  }

  val mainTagKeys: Seq[String] = prefixes.map(prefix => s"${prefix}n_ref") ++
    prefixes.map(prefix => s"expected_${prefix}n_route_relations") ++
    prefixes.map(prefix => s"${prefix}n_name") ++
    prefixes.map(prefix => s"${prefix}n:name") ++
    Seq(
      "fixme",
      "fixmetodo",
      "network:type",
      "survey:date"
    )
}

class NodeTagDiffAnalyzer(before: Tagable, after: Tagable) extends TagDiffAnalyzer(before, after, NodeTagDiffAnalyzer.mainTagKeys)

object RouteTagDiffAnalyzer {
  val mainTagKeys: Seq[String] = Seq(
    "ref",
    "name",
    "note",
    "from",
    "to",
    "network",
    "type",
    "route",
    "network:type"
  )
}

class RouteTagDiffAnalyzer(before: Tagable, after: Tagable) extends TagDiffAnalyzer(before, after, RouteTagDiffAnalyzer.mainTagKeys)

class TagDiffAnalyzer(before: Tagable, after: Tagable, mainTagKeys: Seq[String] = Seq.empty) {

  def diffs: Option[TagDiffs] = {

    val beforeKeys = before.tags.keys.toSet
    val afterKeys = after.tags.keys.toSet

    val allKeys = (beforeKeys ++ afterKeys).toSeq.sorted
    val removedKeys = beforeKeys -- afterKeys
    val addedKeys = afterKeys -- beforeKeys

    val tagDetails = allKeys.map { key =>
      val beforeValue = before.tags(key)
      val afterValue = after.tags(key)

      val action = if (removedKeys.contains(key)) {
        TagDetailType.Delete
      }
      else if (addedKeys.contains(key)) {
        TagDetailType.Add
      }
      else if (beforeValue != afterValue) {
        TagDetailType.Update
      }
      else {
        TagDetailType.Same
      }
      TagDetail(action, key, beforeValue, afterValue)
    }

    val tagDetailMap = tagDetails.map(detail => detail.key -> detail).toMap

    if (tagDetails.exists(_.action != TagDetailType.Same)) {
      val mainDetails = mainTagKeys.filter(key => tagDetailMap.contains(key)).map(key => tagDetailMap(key))
      val extraDetails = tagDetails.filterNot(detail => mainTagKeys.contains(detail.key)).sortBy(_.sortKey)
      Some(TagDiffs(mainDetails, extraDetails))
    }
    else {
      None
    }
  }
}
