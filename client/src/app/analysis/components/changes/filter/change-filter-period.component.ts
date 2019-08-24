import {Component, Input} from "@angular/core";
import {ChangeFilterOption} from "./change-filter-option";

@Component({
  selector: "kpn-change-filter-period",
  template: `
    <div class="row">
      <div [ngClass]="option.level">
        {{option.period.name}}
      </div>
      <div class="count-links">
        <a (click)="option.impactedCountClicked()" class="link">{{option.period.impactedCount}}</a> /
        <a (click)="option.totalCountClicked()" class="link">{{option.period.totalCount}}</a>
      </div>
    </div>
  `,
  styleUrls: ["../../filter/filter.scss"]
})
export class ChangeFilterPeriodComponent {
  @Input() option: ChangeFilterOption;
}
