package kpn.core.replicate

import kpn.api.common.ReplicationId

object ReplicationRequestExecutorDemo {
  def main(args: Array[String]): Unit = {
    println(new ReplicationRequestExecutorImpl().requestStateFile(ReplicationId(5, 829, 46)))
  }
}
