import {FilterOption} from "./filter-option";
import {List} from "immutable";

export class FilterOptionGroup {
  constructor(readonly name: string,
              readonly options: List<FilterOption>) {
  }
}
