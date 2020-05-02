import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {ChangeFilterOptions} from "./change-filter-options";

@Component({
  selector: "kpn-change-filter",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!filterOptions.options.isEmpty()" class="filter">
      <div class="title" i18n="@@change-filter.title">Filter</div>
      <div class="row">
        <div class="count-links" i18n="@@change-filter.legend">
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
