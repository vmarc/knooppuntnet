import { FilterOptionGroup } from './filter-option-group';

export class FilterOptions {
  constructor(
    readonly filteredCount: number,
    readonly totalCount: number,
    readonly groups: ReadonlyArray<FilterOptionGroup>
  ) {}

  static empty(): FilterOptions {
    return new FilterOptions(0, 0, []);
  }

  isEmpty(): boolean {
    return this.totalCount === 0;
  }
}
