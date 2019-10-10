package kpn.core.engine.changes.orphan.node

import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.load.NodeLoader
import kpn.core.load.data.LoadedNode
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.core.util.Log
import kpn.shared.data.raw.RawNode

class OrphanNodeChangeProcessorImpl(
  changeAnalyzer: OrphanNodeChangeAnalyzer,
  createProcessor: OrphanNodeCreateProcessor,
  updateProcessor: OrphanNodeUpdateProcessor,
  deleteProcessor: OrphanNodeDeleteProcessor,
  countryAnalyzer: CountryAnalyzer,
  nodeLoader: NodeLoader
) extends OrphanNodeChangeProcessor {

  private val log = Log(classOf[OrphanNodeChangeProcessorImpl])

  def process(context: ChangeSetContext): ChangeSetChanges = {

    log.debugElapsed {

      val analysis = changeAnalyzer.analyze(context.changeSet)

      val deletes = {
        val deletedNodesBefore = nodeLoader.loadNodes(context.timestampBefore, analysis.deletes.map(_.id)).map(loadedNode => loadedNode.id -> loadedNode).toMap
        val loadedNodeDeletes = analysis.deletes.map(rawNode => LoadedNodeDelete(rawNode, deletedNodesBefore.get(rawNode.id)))
        loadedNodeDeletes.flatMap(n => deleteProcessor.process(context, n))
      }

      val creates = analysis.creates.flatMap(n => createProcessor.process(Some(context), toLoadedNode(n)))

      val updatedNodesAfter = analysis.updates.map(toLoadedNode)
      val updatedNodesBefore = nodeLoader.loadNodes(context.timestampBefore, updatedNodesAfter.map(_.id)).map(loadedNode => loadedNode.id -> loadedNode).toMap
      val (updatedNodesWithBefore, updatedNodesWithoutBefore) = updatedNodesAfter.partition(loadedNodeAfter => updatedNodesBefore.contains(loadedNodeAfter.id))
      val extraCreates = updatedNodesWithoutBefore.flatMap(n => createProcessor.process(Some(context), n))
      val updateNodeChanges = updatedNodesWithBefore.map { loadedNodeAfter =>
        updatedNodesBefore.get(loadedNodeAfter.id) match {
          case Some(loadedNodeBefore) => LoadedNodeChange(loadedNodeBefore, loadedNodeAfter)
          case None => throw new IllegalStateException(s"Cannot find loadedNodeBefore id=${loadedNodeAfter.id}")
        }
      }
      val updates = updateNodeChanges.flatMap(nodeChange => updateProcessor.process(context, nodeChange))

      val allNodeChanges = creates ++ extraCreates ++ updates ++ deletes
      val changeSetChanges = merge(ChangeSetChanges(nodeChanges = allNodeChanges))

      val message = s"added=${(creates ++ extraCreates).size}, updated=${updates.size}, removed=${deletes.size}"
      (message, changeSetChanges)
    }
  }

  private def toLoadedNode(node: RawNode): LoadedNode = {
    val country = countryAnalyzer.country(Seq(node))
    LoadedNode.from(country, node)
  }
}
