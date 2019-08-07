import {FilterOption} from "./filter-option";
import {List} from "immutable";

export class FilterOptionGroup {

  readonly options: List<FilterOption>;

  constructor(readonly name: string,
              ...options: FilterOption[]) {
    this.options = List(options);
  }
}
