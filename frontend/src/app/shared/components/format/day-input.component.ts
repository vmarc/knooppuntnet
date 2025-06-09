import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { DateAdapter } from '@angular/material/core';
import { MatNativeDateModule } from '@angular/material/core';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { DayUtil } from '@app/shared/components/day-util';
import { KpnDateAdapter } from '@app/shared/components/day/kpn-date-adapter';

@Component({
  selector: 'ui-day-input',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field>
      <mat-label>{{ label() }}</mat-label>
      <input matInput [matDatepicker]="picker" [formControl]="date()" />
      <mat-hint>{{ dateFormatString() }}</mat-hint>
      <mat-datepicker-toggle matSuffix [for]="picker" />
      <mat-datepicker #picker />
    </mat-form-field>
  `,
  providers: [
    MatNativeDateModule,
    {
      provide: DateAdapter,
      useClass: KpnDateAdapter,
      deps: [MAT_DATE_LOCALE],
    },
  ],
  imports: [
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    ReactiveFormsModule,
  ],
})
export class DayInputComponent {
  readonly date = input.required<FormControl<Date | null>>();
  readonly label = input.required<string>();

  private readonly matDateLocale = inject(MAT_DATE_LOCALE);

  dateFormatString(): string {
    return DayUtil.formatString(this.matDateLocale.toString());
  }
}
