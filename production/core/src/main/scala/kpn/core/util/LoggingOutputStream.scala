package kpn.core.util

import java.io.OutputStream

class LoggingOutputStream(log: Log) extends OutputStream {

  private var buffer = new StringBuffer(100)

  override def write(b: Int): Unit = {
    if (b == '\n') {
      flush()
    }
    else {
      buffer.append(b)
    }
  }

  override def flush(): Unit = {
    log.debug(buffer.toString)
    buffer = new StringBuffer(100)
    super.flush()
  }

  override def close(): Unit = {
    flush()
    super.close()
  }
}
