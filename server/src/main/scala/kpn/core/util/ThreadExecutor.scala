package kpn.core.util

import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

object ThreadExecutor {

  type ThreadExecutorAction = (
    Int, // index
      Int, // count
      Long // id
    ) => Unit

  def execute(
    poolSize: Int,
    ids: Seq[Long],
  )(action: ThreadExecutorAction): Unit = {

    val routeIds = ids.toVector
    val routeCount = routeIds.size
    val routeIndex = new AtomicInteger(0)

    val es = executor(poolSize)
    for (i <- 0 until poolSize) {
      es.execute(new Runnable() {
        override def run(): Unit = {
          var index = routeIndex.getAndIncrement()
          while (index < routeCount) {
            val routeId = routeIds(index)
            action(index, routeCount, routeId)
            index = routeIndex.getAndIncrement()
          }
        }
      })
    }
    es.shutdown()
    es.awaitTermination(12, TimeUnit.HOURS)
  }

  private def executor(poolSize: Int): ThreadPoolExecutor = {
    new ThreadPoolExecutor(
      poolSize,
      poolSize,
      60L,
      TimeUnit.SECONDS,
      new SynchronousQueue[Runnable]
    )
  }
}
