package kpn.server.analyzer.load

import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp

trait NetworkRelationLoader {
  def load(timestamp: Option[Timestamp], networkId: Long): Option[RawRelation]
}
