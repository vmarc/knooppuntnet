import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-month',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span *ngIf="month === 1" i18n="@@filter.month.january">January</span>
    <span *ngIf="month === 2" i18n="@@filter.month.february">February</span>
    <span *ngIf="month === 3" i18n="@@filter.month.march">March</span>
    <span *ngIf="month === 4" i18n="@@filter.month.april">April</span>
    <span *ngIf="month === 5" i18n="@@filter.month.may">May</span>
    <span *ngIf="month === 6" i18n="@@filter.month.june">June</span>
    <span *ngIf="month === 7" i18n="@@filter.month.july">July</span>
    <span *ngIf="month === 8" i18n="@@filter.month.august">August</span>
    <span *ngIf="month === 9" i18n="@@filter.month.september">September</span>
    <span *ngIf="month === 10" i18n="@@filter.month.october">October</span>
    <span *ngIf="month === 11" i18n="@@filter.month.november">November</span>
    <span *ngIf="month === 12" i18n="@@filter.month.december"> December</span>
  `,
})
export class MonthComponent {
  @Input() month: number;
}
