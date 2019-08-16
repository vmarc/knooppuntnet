// Migrated to Angular: filter.ts
package kpn.client.filter

abstract class Filter[T](val name: String) {
  def passes(element: T): Boolean
}
