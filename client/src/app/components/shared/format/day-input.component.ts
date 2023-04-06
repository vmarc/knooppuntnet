import { ChangeDetectionStrategy } from '@angular/core';
import { Inject } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { DayUtil } from '../day-util';

@Component({
  selector: 'kpn-day-input',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field>
      <mat-label>{{ label }}</mat-label>
      <input matInput [matDatepicker]="picker" [formControl]="date" />
      <mat-hint>{{ dateFormatString() }}</mat-hint>
      <mat-datepicker-toggle matSuffix [for]="picker" />
      <mat-datepicker #picker />
    </mat-form-field>
  `,
})
export class DayInputComponent {
  @Input() date: FormControl<Date | null>;
  @Input() label: string;

  constructor(@Inject(MAT_DATE_LOCALE) private matDateLocale: string) {}

  dateFormatString(): string {
    return DayUtil.formatString(this.matDateLocale);
  }
}
