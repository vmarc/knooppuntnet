import { ChangeDetectionStrategy } from '@angular/core';
import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { Input } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { DateAdapter } from '@angular/material/core';
import { DayUtil } from '../day-util';

@Component({
  selector: 'kpn-day-input',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field>
      <mat-label>{{ label }}</mat-label>
      <input matInput [matDatepicker]="picker" [formControl]="date" />
      <mat-hint>{{ dateFormatString() }}</mat-hint>
      <mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
      <mat-datepicker #picker></mat-datepicker>
    </mat-form-field>
  `,
})
export class DayInputComponent implements OnInit {
  @Input() ngForm: FormGroupDirective;
  @Input() date: FormControl<Date | null>;
  @Input() label: string;

  constructor(
    @Inject(LOCALE_ID) public locale: string,
    private _adapter: DateAdapter<any>,
    @Inject(MAT_DATE_LOCALE) private matDateLocale: string
  ) {}

  ngOnInit(): void {
    this.matDateLocale = DayUtil.localeString(this.locale);
    this._adapter.setLocale(this.matDateLocale);
  }

  dateFormatString(): string {
    return DayUtil.formatString(this.locale);
  }
}
