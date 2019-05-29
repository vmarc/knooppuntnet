// TODO migrate to Angular
package kpn.client.filter

class Filters[T](filters: Filter[T]*) {

  def passes(element: T): Boolean = {
    passesAll(element, filters)
  }

  def filterExcept(elements: Seq[T], filter: Filter[T]): Seq[T] = {
    filtered(elements, filters.filterNot(_.name == filter.name))
  }

  private def filtered(elements: Seq[T], filterCollection: Seq[Filter[T]]): Seq[T] = {
    elements.filter(element => passesAll(element, filterCollection))
  }

  private def passesAll(element: T, filterCollection: Seq[Filter[T]]): Boolean = {
    !filterCollection.exists(f => !f.passes(element))
  }
}
