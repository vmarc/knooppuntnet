package kpn.core.tools.analyzer

import kpn.shared.ReplicationId

trait AnalyzerEngine {

  def load(replicationId: ReplicationId): Unit

  def process(replicationId: ReplicationId): Unit

}
