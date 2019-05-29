// TODO migrate to Angular
package kpn.client.components.changes

import kpn.client.components.common.PageState
import kpn.shared.ChangesPage
import kpn.shared.changes.filter.ChangesParameters

case class ChangesState(
  pageState: PageState[ChangesPage] = PageState(),
  parameters: Option[ChangesParameters] = None
)
