import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'kpn-month',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @switch (month()) {
      @case (1) {
        <span i18n="@@filter.month.january">January</span>
      }
      @case (2) {
        <span i18n="@@filter.month.february">February</span>
      }
      @case (3) {
        <span i18n="@@filter.month.march">March</span>
      }
      @case (4) {
        <span i18n="@@filter.month.april">April</span>
      }
      @case (5) {
        <span i18n="@@filter.month.may">May</span>
      }
      @case (6) {
        <span i18n="@@filter.month.june">June</span>
      }
      @case (7) {
        <span i18n="@@filter.month.july">July</span>
      }
      @case (8) {
        <span i18n="@@filter.month.august">August</span>
      }
      @case (9) {
        <span i18n="@@filter.month.september">September</span>
      }
      @case (10) {
        <span i18n="@@filter.month.october">October</span>
      }
      @case (11) {
        <span i18n="@@filter.month.november">November</span>
      }
      @case (12) {
        <span i18n="@@filter.month.december"> December</span>
      }
    }
  `,
  standalone: true,
})
export class MonthComponent {
  month = input.required<number>();
}
