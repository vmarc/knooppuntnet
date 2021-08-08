package kpn.server.analyzer.load

import kpn.api.custom.Timestamp

trait NetworkIdsLoader {

  def load(timestamp: Timestamp): Seq[Long]

}
