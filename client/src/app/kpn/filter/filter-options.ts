import {FilterOptionGroup} from "./filter-option-group";
import {List} from "immutable";

export class FilterOptions {
  constructor(readonly filteredCount: number,
              readonly totalCount: number,
              readonly groups: List<FilterOptionGroup>) {
  }

  static empty(): FilterOptions {
    return new FilterOptions(0, 0, List());
  }
}
