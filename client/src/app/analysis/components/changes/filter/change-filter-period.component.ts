import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/core.state';
import { ChangeOption } from '../../../changes/store/changes.actions';

/* tslint:disable:template-i18n */
@Component({
  selector: 'kpn-change-filter-period',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="row">
      <div [ngClass]="option.level">
        <kpn-month
          *ngIf="option.level === 'month'"
          [month]="option.month"
        ></kpn-month>
        <ng-container *ngIf="option.level !== 'month'">{{
          option.name
        }}</ng-container>
        <ng-container *ngIf="option.current">&nbsp;&larr;</ng-container>
      </div>
      <div class="count-links">
        <a (click)="impactedCountClicked()" class="link">{{
          option.impactedCount
        }}</a>
        /
        <a (click)="totalCountClicked()" class="link">{{
          option.totalCount
        }}</a>
      </div>
    </div>
  `,
  styleUrls: ['../../filter/filter.scss'],
})
export class ChangeFilterPeriodComponent {
  @Input() option: ChangesFilterOption;
  @Output() changeOption = new EventEmitter<ChangeOption>();

  constructor(private store: Store<AppState>) {}

  impactedCountClicked(): void {
    this.countClicked(true);
  }

  totalCountClicked(): void {
    this.countClicked(false);
  }

  private countClicked(impact: boolean): void {
    const option: ChangeOption = {
      year: this.option.year,
      month: this.option.month,
      day: this.option.day,
      impact,
    };
    this.changeOption.emit(option);
  }
}
