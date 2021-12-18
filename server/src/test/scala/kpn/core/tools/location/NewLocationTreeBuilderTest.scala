package kpn.core.tools.location

import kpn.core.doc.LocationPath
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationTree

class NewLocationTreeBuilderTest extends UnitTest {

  test("tree building") {

    val locations = Seq(
      location("fr", Seq.empty),
      location("fr-1-01", Seq(LocationPath(Seq("fr")))),
      location("fr-1-02", Seq(LocationPath(Seq("fr")))),
      location(
        "fr-2-CDC-A",
        Seq(
          LocationPath(Seq("fr", "fr-1-01")),
          LocationPath(Seq("fr", "fr-1-02")),
        )
      ),
      location(
        "fr-2-CDC-B",
        Seq(
          LocationPath(Seq("fr", "fr-1-01"))
        )
      ),
      location("fr-3-c1", Seq(LocationPath(Seq("fr", "fr-1-01", "fr-2-CDC-A")))),
      location("fr-3-c2", Seq(LocationPath(Seq("fr", "fr-1-01", "fr-2-CDC-A")))),
      location("fr-3-c3", Seq(LocationPath(Seq("fr", "fr-1-01", "fr-2-CDC-A")))),
      location("fr-3-c4", Seq(LocationPath(Seq("fr", "fr-1-02", "fr-2-CDC-A")))),
      location("fr-3-c5", Seq(LocationPath(Seq("fr", "fr-1-02", "fr-2-CDC-A")))),
    )

    val trees = new NewLocationTreeBuilder().buildTree(locations).children.get
    trees.foreach { tree =>
      printTree(locations, tree)
    }
  }

  private def printTree(locations: Seq[LocationData], tree: LocationTree, level: Int = 0): Unit = {
    locations.find(_.id == tree.name).foreach { location =>
      val spaces = 1.to(level).map(x => "  ").mkString
      val childCount = if (tree.children.isDefined) s" (${tree.children.get.size})" else ""
      println(s"$spaces - `${location.id}` ${location.name} $childCount")
      tree.children.toSeq.flatten.foreach { child =>
        printTree(locations, child, level + 1)
      }
    }
  }

  private def location(id: String, paths: Seq[LocationPath]): LocationData = {
    LocationData(
      id,
      paths,
      "",
      Seq.empty,
      null
    )
  }
}
