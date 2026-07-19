package kpn.api.common.data;

import kpn.api.custom.Timestamp;

public record MetaData(
  Long version,
  Timestamp timestamp,
  Long changeSetId
) {
}

/*
package kpn.api.common.data

import kpn.api.custom.Timestamp

case class MetaData(version: Long, timestamp: Timestamp, changeSetId: Long) extends Meta

*/
