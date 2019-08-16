// Migrated to Angular
package kpn.client.components.network.nodes

import kpn.client.common.NetworkPageArgs
import kpn.client.components.common.PageProps
import kpn.client.components.network.NetworkPageProps
import kpn.shared.TimeInfo
import kpn.shared.network.NetworkSummary

case class NetworkNodesProps(
  timeInfo: TimeInfo,
  args: NetworkPageArgs,
  pageProps: PageProps,
  networkName: Option[String],
  networkSummary: Option[NetworkSummary]
) extends NetworkPageProps

