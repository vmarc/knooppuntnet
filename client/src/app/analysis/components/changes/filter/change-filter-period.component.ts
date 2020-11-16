import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {ChangeFilterOption} from './change-filter-option';

/* tslint:disable:template-i18n */
@Component({
  selector: 'kpn-change-filter-period',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="row">
      <div [ngClass]="option.level">
        <ng-container *ngIf="option.level === 'month'">
          <span *ngIf="option.period.name === '01'" i18n="@@filter.month.january">January</span>
          <span *ngIf="option.period.name === '02'" i18n="@@filter.month.february">February</span>
          <span *ngIf="option.period.name === '03'" i18n="@@filter.month.march">March</span>
          <span *ngIf="option.period.name === '04'" i18n="@@filter.month.april">April</span>
          <span *ngIf="option.period.name === '05'" i18n="@@filter.month.may">May</span>
          <span *ngIf="option.period.name === '06'" i18n="@@filter.month.june">June</span>
          <span *ngIf="option.period.name === '07'" i18n="@@filter.month.july">July</span>
          <span *ngIf="option.period.name === '08'" i18n="@@filter.month.august">August</span>
          <span *ngIf="option.period.name === '09'" i18n="@@filter.month.september">September</span>
          <span *ngIf="option.period.name === '10'" i18n="@@filter.month.october">October</span>
          <span *ngIf="option.period.name === '11'" i18n="@@filter.month.november">November</span>
          <span *ngIf="option.period.name === '12'" i18n="@@filter.month.december">December</span>
        </ng-container>
        <ng-container *ngIf="option.level !== 'month'">
          {{option.period.name}}
        </ng-container>
        <ng-container *ngIf="option.period.current">*</ng-container>
      </div>
      <div class="count-links">
        <a (click)="option.impactedCountClicked()" class="link">{{option.period.impactedCount}}</a> /
        <a (click)="option.totalCountClicked()" class="link">{{option.period.totalCount}}</a>
      </div>
    </div>
  `,
  styleUrls: ['../../filter/filter.scss']
})
export class ChangeFilterPeriodComponent {
  @Input() option: ChangeFilterOption;
}
