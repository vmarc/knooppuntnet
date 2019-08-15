// Migrated to Angular: _network-facts-page.component.ts
package kpn.client.components.network.facts

import kpn.client.common.NetworkPageArgs
import kpn.client.components.common.PageProps
import kpn.client.components.network.NetworkPageProps
import kpn.shared.network.NetworkSummary

case class NetworkFactsProps(
  args: NetworkPageArgs,
  pageProps: PageProps,
  networkName: Option[String],
  networkSummary: Option[NetworkSummary]
) extends NetworkPageProps
