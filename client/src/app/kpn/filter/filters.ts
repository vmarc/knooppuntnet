import { Filter } from './filter';
import { List } from 'immutable';

export class Filters<T> {
  readonly filters: List<Filter<T>>;

  constructor(...filters: Filter<T>[]) {
    this.filters = List(filters);
  }

  passes(element: T): boolean {
    return this.passesAll(element, this.filters);
  }

  filterExcept(elements: T[], filter: Filter<T>): T[] {
    return this.filtered(
      elements,
      this.filters.filterNot((f) => f.name === filter.name)
    );
  }

  private filtered(elements: T[], filterCollection: List<Filter<T>>): T[] {
    return elements.filter((element) =>
      this.passesAll(element, filterCollection)
    );
  }

  private passesAll(element: T, filterCollection: List<Filter<T>>): boolean {
    return filterCollection.findIndex((f) => !f.passes(element)) < 0;
  }
}
