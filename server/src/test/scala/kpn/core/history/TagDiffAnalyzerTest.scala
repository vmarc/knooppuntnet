package kpn.core.history

import kpn.api.common.SharedTestObjects
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class TagDiffAnalyzerTest extends UnitTest with SharedTestObjects {

  test("tag differences") {

    val mainTagKeys = Seq("aaa", "bbb")
    val before = newNode(1, tags = Tags.from("aaa" -> "1", "ccc" -> "2", "ddd" -> "3"))
    val after = newNode(2, tags = Tags.from("bbb" -> "4", "ccc" -> "2", "ddd" -> "5", "eee" -> "6"))

    new TagDiffAnalyzer(before, after, mainTagKeys).diffs.value should matchTo(
      TagDiffs(
        Seq(
          TagDetail(TagDetailType.Delete, "aaa", Some("1"), None),
          TagDetail(TagDetailType.Add, "bbb", None, Some("4"))
        ),
        Seq(
          TagDetail(TagDetailType.Add, "eee", None, Some("6")),
          TagDetail(TagDetailType.Same, "ccc", Some("2"), Some("2")),
          TagDetail(TagDetailType.Update, "ddd", Some("3"), Some("5"))
        )
      )
    )
  }

  test("tag detail sorting order") {

    val before = newNode(1, tags = Tags.from("bbb" -> "5", "ddd" -> "1", "ccc" -> "3"))
    val after = newNode(2, tags = Tags.from("ddd" -> "1", "aaa" -> "2", "ccc" -> "4"))

    new TagDiffAnalyzer(before, after, Seq()).diffs.value should matchTo(
      TagDiffs(
        Seq(
        ),
        Seq(
          TagDetail(TagDetailType.Add, "aaa", None, Some("2")),
          TagDetail(TagDetailType.Delete, "bbb", Some("5"), None),
          TagDetail(TagDetailType.Same, "ddd", Some("1"), Some("1")),
          TagDetail(TagDetailType.Update, "ccc", Some("3"), Some("4"))
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
    val after = newNode(1, tags = Tags.empty)
    val diffs = new TagDiffAnalyzer(before, after, NodeTagDiffAnalyzer.mainTagKeys).diffs
    diffs.get.mainTags.map(_.key) should contain(tagKey)
  }

  private def assertRouteTagDiffAnalyzerMainTag(tagKey: String): Unit = {
    val before = newNode(1, tags = Tags.from(tagKey -> ""))
    val after = newNode(1, tags = Tags.empty)
    val diffs = new TagDiffAnalyzer(before, after, RouteTagDiffAnalyzer.mainTagKeys).diffs
    diffs.get.mainTags.map(_.key) should contain(tagKey)
  }

}
