package kpn.core.history

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.data.Tagable
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.RouteScopeLetter
import kpn.api.custom.RouteTypeLetter

object NodeTagDiffAnalyzer {

  private val prefixes = RouteScope.values.flatMap { routeScope =>
    RouteType.values.map { routeType =>
      val routeTypeLetter = RouteTypeLetter.letter(routeType)
      val routeScopeLetter = RouteScopeLetter.letter(routeScope)
      s"$routeScopeLetter$routeTypeLetter"
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

    val beforeKeys = before.tags.map(_.key).toSet
    val afterKeys = after.tags.map(_.key).toSet

    val allKeys = (beforeKeys ++ afterKeys).toSeq.sorted
    val removedKeys = beforeKeys -- afterKeys
    val addedKeys = afterKeys -- beforeKeys

    val tagDiffs = allKeys.map { key =>
      val beforeValue = before.tagValue(key)
      val afterValue = after.tagValue(key)

      val action = if (removedKeys.contains(key)) {
        TagDiffType.delete
      }
      else if (addedKeys.contains(key)) {
        TagDiffType.add
      }
      else if (beforeValue != afterValue) {
        TagDiffType.update
      }
      else {
        TagDiffType.same
      }
      TagDiff(action, key, beforeValue, afterValue)
    }

    val tagDiffMap = tagDiffs.map(detail => detail.key -> detail).toMap

    Option.when(tagDiffs.exists(_.action != TagDiffType.same)) {
      val mainDiffs = mainTagKeys.filter(key => tagDiffMap.contains(key)).map(key => tagDiffMap(key))
      val extraDiffs = tagDiffs.filterNot(detail => mainTagKeys.contains(detail.key)).sortBy(_.sortKey)
      TagDiffs(mainDiffs, extraDiffs)
    }
  }
}
