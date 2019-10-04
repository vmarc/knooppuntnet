package kpn.core.engine.changes.orphan.node

import kpn.core.engine.changes.data.AnalysisData
import kpn.core.load.data.LoadedNode
import kpn.core.repository.AnalysisRepository
import kpn.core.test.TestData
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NodeInfo
import kpn.shared.data.Node
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanNodeCreateProcessorTest extends FunSuite with Matchers with MockFactory {

  test("new orphan node is saved to database") {

    val d = new NewOrphanNodeSetup

    d.processor.process(None, d.loadedNode)

    (d.analysisRepository.saveNode _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo.id should equal(d.loadedNode.id)
        nodeInfo.orphan should equal(true)
        nodeInfo.name should equal(d.loadedNode.name)
        nodeInfo.facts should equal(Seq())
        true
      }
    )
  }

  test("new orphan node is added to 'watched' and removed from 'ignored'") {

    val d = new NewOrphanNodeSetup

    d.processor.process(None, d.loadedNode)

    d.analysisData.orphanNodes.watched.contains(d.loadedNode.id) should equal(true)
  }

  private class NewOrphanNodeSetup extends Setup {
    analysisData.orphanNodes.watched.add(loadedNode.id)
  }

  private class Setup {

    val node: Node = new TestData() {
      networkNode(1001, "01")
    }.data.nodes(1001)

    val loadedNode: LoadedNode = LoadedNode(
      country = Some(Country.nl),
      networkTypes = Seq.empty,
      name = "01",
      node = node
    )

    val analysisData: AnalysisData = AnalysisData()
    val analysisRepository: AnalysisRepository = stub[AnalysisRepository]

    val processor: OrphanNodeCreateProcessor = new OrphanNodeCreateProcessorImpl(
      analysisData,
      analysisRepository
    )
  }

}
