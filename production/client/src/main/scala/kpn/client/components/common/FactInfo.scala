// TODO migrate to Angular
package kpn.client.components.common

import kpn.shared.Fact
import kpn.shared.common.Ref

case class FactInfo(
  fact: Fact,
  networkRef: Option[Ref] = None,
  routeRef: Option[Ref] = None,
  nodeRef: Option[Ref] = None
)
