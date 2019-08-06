import {Filter} from "./filter";
import {Filters} from "./filters";
import {List} from "immutable";
import {FilterOptionGroup} from "./filter-option-group";
import {FilterOption} from "./filter-option";

export class BooleanFilter<T> extends Filter<T> {

  constructor(name: string,
              readonly criterium: boolean,
              readonly booleanPropertyAccessor: (T) => boolean,
              readonly all: () => void,
              readonly yes: () => void,
              readonly no: () => void) {
    super(name);
  }

  passes(element: T): boolean {
    if (this.criterium == null) {
      return true;
    }
    if (this.criterium) {
      return this.booleanPropertyAccessor(element);
    }
    return !this.booleanPropertyAccessor(element);
  }

  filterOptions(allFilters: Filters<T>, elements: List<T>): FilterOptionGroup {

    const filteredElements = allFilters.filterExcept(elements, this);
    const yesElements = filteredElements.filter(e => this.booleanPropertyAccessor(e));
    const noElements = filteredElements.filterNot(e => this.booleanPropertyAccessor(e));
    const active = !filteredElements.isEmpty() && !yesElements.isEmpty() && !noElements.isEmpty();

    if (active) {
      const allOption = new FilterOption(
        "all",
        filteredElements.size,
        this.criterium == null,
        this.all
      );

      const yesOption = new FilterOption(
        "yes",
        yesElements.size,
        this.criterium == true,
        this.yes
      );

      const noOption = new FilterOption(
        "no",
        noElements.size,
        this.criterium == false,
        this.no
      );

      return new FilterOptionGroup(this.name, List([allOption, yesOption, noOption]));
    }
    return null;
  }

}
