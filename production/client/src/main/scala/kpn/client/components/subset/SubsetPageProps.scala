package kpn.client.components.subset

import kpn.client.common.SubsetPageArgs
import kpn.client.components.common.PageProps
import kpn.shared.Fact
import kpn.shared.TimeInfo

case class SubsetPageProps(
  timeInfo: TimeInfo,
  args: SubsetPageArgs,
  pageProps: PageProps,
  fact: Option[Fact] = None
)
