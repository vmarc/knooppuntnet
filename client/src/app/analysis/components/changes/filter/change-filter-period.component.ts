import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {ChangeFilterOption} from "./change-filter-option";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-change-filter-period",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="row">
      <div [ngClass]="option.level">
        {{option.period.name}}
        <ng-container *ngIf="option.period.current">*</ng-container>
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
