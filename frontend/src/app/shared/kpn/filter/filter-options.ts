import { List } from 'immutable';
import { FilterOptionGroup } from './filter-option-group';

export class FilterOptions {
  constructor(
    readonly filteredCount: number,
    readonly totalCount: number,
    readonly groups: List<FilterOptionGroup>
  ) {}

  static empty(): FilterOptions {
    return new FilterOptions(0, 0, List());
  }

  isEmpty(): boolean {
    return this.totalCount === 0;
  }
}
