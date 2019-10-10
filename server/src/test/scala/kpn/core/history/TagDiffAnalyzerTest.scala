package kpn.core.history

import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TagDiffAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  test("tag differences") {

    val mainTagKeys = Seq("aaa", "bbb")
    val before = newNode(1, tags = Tags.from("aaa" -> "1", "ccc" -> "2", "ddd" -> "3"))
    val after = newNode(2, tags = Tags.from("bbb" -> "4", "ccc" -> "2", "ddd" -> "5", "eee" -> "6"))

    val expected = TagDiffs(
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

    new TagDiffAnalyzer(before, after, mainTagKeys).diffs should equal(Some(expected))
  }

  test("tag detail sorting order") {

    val before = newNode(1, tags = Tags.from("bbb" -> "5", "ddd" -> "1", "ccc" -> "3"))
    val after = newNode(2, tags = Tags.from("ddd" -> "1", "aaa" -> "2", "ccc" -> "4"))

    val expected = TagDiffs(
      Seq(
      ),
      Seq(
        TagDetail(TagDetailType.Add, "aaa", None, Some("2")),
        TagDetail(TagDetailType.Delete, "bbb", Some("5"), None),
        TagDetail(TagDetailType.Same, "ddd", Some("1"), Some("1")),
        TagDetail(TagDetailType.Update, "ccc", Some("3"), Some("4"))
      )
    )

    new TagDiffAnalyzer(before, after, Seq()).diffs should equal(Some(expected))
  }
}
