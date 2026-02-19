import { FilterOption } from './filter-option';

export class FilterOptionGroup {
  readonly options: ReadonlyArray<FilterOption>;

  constructor(
    readonly name: string,
    ...options: ReadonlyArray<FilterOption>
  ) {
    this.options = options;
  }
}
