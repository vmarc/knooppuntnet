// TODO migrate to Angular
package kpn.client.components.subset.nodes

import kpn.client.filter.TimeFilterKind

case class SubsetOrphanNodeFilterCriteria(
  lastUpdated: TimeFilterKind.Value = TimeFilterKind.ALL
)
