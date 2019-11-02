package kpn.core.history

import kpn.shared.data.Tagable
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs

object NodeTagDiffAnalyzer {
  val mainTagKeys: Seq[String] = Seq(
    "rcn_ref",
    "expected_rcn_route_relations",
    "rwn_ref",
    "expected_rwn_route_relations",
    "rhn_ref",
    "expected_rhn_route_relations",
    "rmn_ref",
    "expected_rmn_route_relations",
    "rpn_ref",
    "expected_rpn_route_relations",
    "rin_ref",
    "expected_rin_route_relations",
    "fixme",
    "fixmetodo",
    "network:type"
  )
}

class NodeTagDiffAnalyzer(before: Tagable, after: Tagable) extends TagDiffAnalyzer(before, after, NodeTagDiffAnalyzer.mainTagKeys)

object RouteTagDiffAnalyzer {
  val mainTagKeys: Seq[String] = Seq(
    "network",
    "type",
    "route",
    "note",
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
