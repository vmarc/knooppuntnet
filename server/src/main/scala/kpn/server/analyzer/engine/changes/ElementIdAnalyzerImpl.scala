package kpn.server.analyzer.engine.changes

import kpn.core.FastUtil
import kpn.server.analyzer.engine.context.ChangeElementIds
import kpn.server.analyzer.engine.context.ElementIdMap
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

@Component
class ElementIdAnalyzerImpl(
  implicit val analysisExecutionContext: ExecutionContext
) extends ElementIdAnalyzer {

  /*
  * Finds the ids of all the elements that contain at least 1 of given elements.
  */
  def referencedBy(elementIdMap: ElementIdMap, elementIds: ChangeElementIds): Set[Long] = {
    val keys = elementIdMap.ids.toSeq.sorted
    val batchSize = Math.max(5000, keys.size / 20)
    val futures = keys.sliding(batchSize, batchSize).map { keysSubset =>
      Future {
        keysSubset.filter { key =>
          if (elementIds.relationIds.contains(key)) {
            true
          }
          else {
            elementIdMap.get(key) match {
              case None => false
              case Some(mapElementIds) =>
                FastUtil.contains(mapElementIds.relationIds, key) ||
                  elementIds.relationIds.exists(id => FastUtil.contains(mapElementIds.relationIds, id)) ||
                  elementIds.wayIds.exists(id => FastUtil.contains(mapElementIds.wayIds, id)) ||
                  elementIds.nodeIds.exists(id => FastUtil.contains(mapElementIds.nodeIds, id))
            }
          }
        }
      }
    }

    val futuresSeq = Future.sequence(futures)
    Await.result(futuresSeq, Duration(1, TimeUnit.MINUTES)).flatten.toSet
  }
}
