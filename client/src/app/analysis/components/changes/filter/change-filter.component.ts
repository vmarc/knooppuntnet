import {Component, Input} from "@angular/core";
import {ChangeFilterOptions} from "./change-filter-options";

@Component({
  selector: "kpn-change-filter",
  template: `
    <div class="filter">
      <div class="title">Filter</div>
      <div class="row">
        <div class="count-links">
          impacted / all
        </div>
      </div>
      <div *ngFor="let option of filterOptions.options">
        <kpn-change-filter-period [option]="option"></kpn-change-filter-period>
      </div>
    </div>
  `,
  styleUrls: ["../../filter/filter.scss"]
})
export class ChangeFilterComponent {
  @Input() filterOptions: ChangeFilterOptions;
}
