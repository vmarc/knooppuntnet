import {Component, Input} from "@angular/core";
import {FilterOptions} from "../../../kpn/filter/filter-options";

@Component({
  selector: "kpn-filter-title",
  template: `
    <div>
      Filter
      {{filterOptions.filteredCount}}/{{filterOptions.totalCount}}
    </div>
  `
})
export class FilterTitleComponent {
  @Input() filterOptions: FilterOptions;
}
