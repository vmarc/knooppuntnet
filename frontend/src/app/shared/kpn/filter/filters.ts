import { Filter } from './filter';

export class Filters<T> {
  readonly filters: ReadonlyArray<Filter<T>>;

  constructor(...filters: ReadonlyArray<Filter<T>>) {
    this.filters = filters;
  }

  passes(element: T): boolean {
    return this.passesAll(element, this.filters);
  }

  filterExcept(elements: ReadonlyArray<T>, filter: Filter<T>): T[] {
    return this.filtered(
      elements,
      this.filters.filter((f) => f.name !== filter.name)
    );
  }

  private filtered(elements: ReadonlyArray<T>, filterCollection: ReadonlyArray<Filter<T>>): T[] {
    return elements.filter((element) => this.passesAll(element, filterCollection));
  }

  private passesAll(element: T, filterCollection: ReadonlyArray<Filter<T>>): boolean {
    return filterCollection.findIndex((f) => !f.passes(element)) < 0;
  }
}
