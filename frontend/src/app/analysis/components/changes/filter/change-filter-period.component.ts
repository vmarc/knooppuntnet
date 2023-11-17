import { NgClass, NgIf } from '@angular/common';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { ChangesFilterOption } from '@api/common/changes/filter';
import { ChangeOption } from '@app/kpn/common';
import { MonthComponent } from './month.component';

@Component({
  selector: 'kpn-change-filter-period',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div class="row">
      <div [ngClass]="option.level">
        <kpn-month *ngIf="option.level === 'month'" [month]="option.month" />
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
  styleUrl: '../../filter/filter.scss',
  standalone: true,
  imports: [NgClass, NgIf, MonthComponent],
})
export class ChangeFilterPeriodComponent {
  @Input() option: ChangesFilterOption;
  @Output() optionSelected = new EventEmitter<ChangeOption>();

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
    this.optionSelected.emit(option);
  }
}
