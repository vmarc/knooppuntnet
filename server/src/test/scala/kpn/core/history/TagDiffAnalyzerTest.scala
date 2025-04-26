package kpn.core.history

import kpn.api.common.SharedTestObjects
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class TagDiffAnalyzerTest extends UnitTest with SharedTestObjects {

  test("tag differences") {

    val mainTagKeys = Seq("aaa", "bbb")
    val before = newNode(1, tags = Tags.from("aaa" -> "1", "ccc" -> "2", "ddd" -> "3"))
    val after = newNode(2, tags = Tags.from("bbb" -> "4", "ccc" -> "2", "ddd" -> "5", "eee" -> "6"))

    assertEqual(
      new TagDiffAnalyzer(before, after, mainTagKeys).diffs,
      Some(
        TagDiffs(
          Seq(
            TagDiff.delete("aaa", "1"),
            TagDiff.add("bbb", "4")
          ),
          Seq(
            TagDiff.add("eee", "6"),
            TagDiff.same("ccc", "2"),
            TagDiff.update("ddd", "3", "5")
          )
        )
      )
    )
  }

  test("tag detail sorting order") {

    val before = newNode(1, tags = Tags.from("bbb" -> "5", "ddd" -> "1", "ccc" -> "3"))
    val after = newNode(2, tags = Tags.from("ddd" -> "1", "aaa" -> "2", "ccc" -> "4"))

    assertEqual(
      new TagDiffAnalyzer(before, after, Seq.empty).diffs,
      Some(
        TagDiffs(
          Seq(
          ),
          Seq(
            TagDiff.add("aaa", "2"),
            TagDiff.delete("bbb", "5"),
            TagDiff.same("ddd", "1"),
            TagDiff.update("ccc", "3", "4")
          )
        )
      )
    )
  }

  test("node tag diff analyzer main tags") {
    assertNodeTagDiffAnalyzerMainTag("rcn_ref")
    assertNodeTagDiffAnalyzerMainTag("expected_lwn_route_relations")
    assertNodeTagDiffAnalyzerMainTag("iin_name")
    assertNodeTagDiffAnalyzerMainTag("ncn:name")
    assertNodeTagDiffAnalyzerMainTag("fixme")
    assertNodeTagDiffAnalyzerMainTag("fixmetodo")
    assertNodeTagDiffAnalyzerMainTag("network:type")
  }

  test("route tag diff analyzer main tags") {
    assertRouteTagDiffAnalyzerMainTag("network")
    assertRouteTagDiffAnalyzerMainTag("type")
    assertRouteTagDiffAnalyzerMainTag("route")
    assertRouteTagDiffAnalyzerMainTag("name")
    assertRouteTagDiffAnalyzerMainTag("note")
    assertRouteTagDiffAnalyzerMainTag("network:type")
  }

  private def assertNodeTagDiffAnalyzerMainTag(tagKey: String): Unit = {
    val before = newNode(1, tags = Tags.from(tagKey -> ""))
    val after = newNode(1, tags = Seq.empty)
    val diffs = new TagDiffAnalyzer(before, after, NodeTagDiffAnalyzer.mainTagKeys).diffs
    diffs.get.mainTags.map(_.key) should contain(tagKey)
  }

  private def assertRouteTagDiffAnalyzerMainTag(tagKey: String): Unit = {
    val before = newNode(1, tags = Tags.from(tagKey -> ""))
    val after = newNode(1, tags = Seq.empty)
    val diffs = new TagDiffAnalyzer(before, after, RouteTagDiffAnalyzer.mainTagKeys).diffs
    diffs.get.mainTags.map(_.key) should contain(tagKey)
  }
}
