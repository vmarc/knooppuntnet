package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.changes.changes.ElementIds
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
  def referencedBy(elementIdMap: ElementIdMap, elementIds: ElementIds): Set[Long] = {
    val keys = elementIdMap.ids.toSeq
    val batchSize = Math.max(5000, keys.size / 20)
    val futures = keys.sliding(batchSize, batchSize).map { keysSubset =>
      Future {
        keysSubset.filter { key =>
          elementIdMap.get(key) match {
            case None => false
            case Some(mapElementIds) =>
              mapElementIds.relationIds.contains(key) ||
                elementIds.relationIds.exists(mapElementIds.relationIds.contains) ||
                elementIds.wayIds.exists(mapElementIds.wayIds.contains) ||
                elementIds.nodeIds.exists(mapElementIds.nodeIds.contains)
          }
        }
      }
    }

    val futuresSeq = Future.sequence(futures)
    Await.result(futuresSeq, Duration(1, TimeUnit.MINUTES)).flatten.toSet
  }

}
