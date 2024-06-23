package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Relation
import kpn.core.data.Data

class StructureTestSetup(val data: Data) {

  def elementGroups(traceEnabled: Boolean = false): Seq[Seq[String]] = {
    val elementGroups = StructureElementAnalyzer.analyze(relation.members, traceEnabled)
    if (traceEnabled) {
      println("\nResult:")
      elementGroups.zipWithIndex.map { case (elementGroup, groupIndex) =>
        elementGroup.elements.zipWithIndex.foreach { case (element, elementIndex) =>
          println(s"  group=${groupIndex + 1}, element=${elementIndex + 1}: ${element.string}")
        }
      }
    }
    elementGroups.map(_.elements.map(_.string))
  }

  def reference(traceEnabled: Boolean = false): Seq[String] = {
    val referenceStructure = new RouteLinkAnalyzer(traceEnabled).analyze(relation)
    val strings = referenceStructure.links.zipWithIndex.map { case (wayInfo, index) => s"${index + 1} ${wayInfo.reportString}" }
    if (traceEnabled) println()
    if (traceEnabled) strings.foreach(println)
    if (traceEnabled) println()
    strings
  }

  def structure(traceEnabled: Boolean = false): TestStructure = {
    TestStructure.from(new StructureAnalyzer(traceEnabled).analyze(relation))
  }

  private def relation: Relation = {
    data.relations(1)
  }
}
