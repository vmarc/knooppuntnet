package kpn.core.util

import java.util.concurrent.atomic.AtomicInteger

object Progress {
  def apply(totalCount: Long): Progress = {
    new Progress(totalCount)
  }
}

class Progress(totalCount: Long) {
  val count = new AtomicInteger(0)
  val progress = new AtomicInteger(0)

  def next(): (Int, String) = {
    val index = count.incrementAndGet()
    val currentProgress = Math.floor((index * 100d) / totalCount).toInt
    if (currentProgress > progress.get()) {
      progress.set(currentProgress)
    }
    (index, s"${progress.get()}% $index/$totalCount")
  }

  def get(): String = {
    val (_, message) = next()
    message
  }
}
