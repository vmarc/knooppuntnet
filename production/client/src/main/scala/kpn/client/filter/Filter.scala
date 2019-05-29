// TODO migrate to Angular
package kpn.client.filter

abstract class Filter[T](val name: String) {
  def passes(element: T): Boolean
}
