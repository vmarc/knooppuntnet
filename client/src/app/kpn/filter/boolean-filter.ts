import { Filter } from './filter';
import { Filters } from './filters';
import { List } from 'immutable';
import { FilterOptionGroup } from './filter-option-group';
import { FilterOption } from './filter-option';

export class BooleanFilter<T> extends Filter<T> {
  constructor(
    name: string,
    private readonly criterium: boolean,
    private readonly getter: (row: T) => boolean,
    private readonly all: () => void,
    private readonly yes: () => void,
    private readonly no: () => void
  ) {
    super(name);
  }

  passes(element: T): boolean {
    if (this.criterium == null) {
      return true;
    }
    if (this.criterium) {
      return this.getter(element);
    }
    return !this.getter(element);
  }

  filterOptions(allFilters: Filters<T>, elements: List<T>): FilterOptionGroup {
    const filteredElements = allFilters.filterExcept(elements, this);
    const yesElements = filteredElements.filter((e) => this.getter(e));
    const noElements = filteredElements.filterNot((e) => this.getter(e));
    const active =
      !filteredElements.isEmpty() &&
      !yesElements.isEmpty() &&
      !noElements.isEmpty();

    if (active) {
      const allOption = new FilterOption(
        'all',
        filteredElements.size,
        this.criterium == null,
        this.all
      );

      const yesOption = new FilterOption(
        'yes',
        yesElements.size,
        this.criterium === true,
        this.yes
      );

      const noOption = new FilterOption(
        'no',
        noElements.size,
        this.criterium === false,
        this.no
      );

      return new FilterOptionGroup(this.name, allOption, yesOption, noOption);
    }
    return null;
  }
}
