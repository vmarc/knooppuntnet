// Migrated to Angular: filter-option.ts
package kpn.client.filter

import japgolly.scalajs.react.Callback

case class FilterOption(
  name: String,
  count: Int,
  selected: Boolean,
  updateState: Callback = Callback.empty
) {

  def isEmpty: Boolean = count == 0

  def sameAs(other: FilterOption): Boolean = {
    // comparison without the callback
    name == other.name && count == other.count && selected == other.selected
  }
}
