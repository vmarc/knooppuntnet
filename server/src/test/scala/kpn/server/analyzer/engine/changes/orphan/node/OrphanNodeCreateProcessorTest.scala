package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.NodeInfo
import kpn.api.common.data.Node
import kpn.api.custom.Country
import kpn.core.test.TestData
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NodeInfoBuilderImpl
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

    d.analysisContext.data.orphanNodes.watched.contains(d.loadedNode.id) should equal(true)
  }

  private class NewOrphanNodeSetup extends Setup {
    analysisContext.data.orphanNodes.watched.add(loadedNode.id)
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

    val analysisContext: AnalysisContext = new AnalysisContext()
    val analysisRepository: AnalysisRepository = stub[AnalysisRepository]
    val tileCalculator = new TileCalculatorImpl()
    val nodeTileAnalyzer = new NodeTileAnalyzerImpl(tileCalculator)
    private val nodeLocationAnalyzer = stub[NodeLocationAnalyzer]
    (nodeLocationAnalyzer.locate _).when(*, *).returns(None)
    val nodeInfoBuilder = new NodeInfoBuilderImpl(nodeTileAnalyzer, nodeLocationAnalyzer)

    val processor: OrphanNodeCreateProcessor = new OrphanNodeCreateProcessorImpl(
      analysisContext,
      analysisRepository,
      nodeInfoBuilder
    )
  }

}
