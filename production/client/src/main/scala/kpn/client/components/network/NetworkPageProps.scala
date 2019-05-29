// TODO migrate to Angular
package kpn.client.components.network

import kpn.client.common.NetworkPageArgs
import kpn.client.components.common.PageProps
import kpn.shared.network.NetworkSummary

trait NetworkPageProps {
  def args: NetworkPageArgs
  def pageProps: PageProps
  def networkName: Option[String]
  def networkSummary: Option[NetworkSummary]
}
