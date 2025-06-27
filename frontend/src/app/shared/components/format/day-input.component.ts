import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { NzDatePickerComponent } from 'ng-zorro-antd/date-picker';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzFormControlComponent } from 'ng-zorro-antd/form';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';

@Component({
  selector: 'ui-day-input',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-form-item nzHasFeedback>
      <nz-form-label>{{ label() }}</nz-form-label>
      <nz-form-control>
        <nz-date-picker [formControl]="date()" />
      </nz-form-control>
    </nz-form-item>
  `,
  imports: [
    ReactiveFormsModule,
    NzFormLabelComponent,
    NzFormControlComponent,
    NzDatePickerComponent,
    NzFormItemComponent,
  ],
})
export class DayInputComponent {
  readonly date = input.required<FormControl<Date | null>>();
  readonly label = input.required<string>();
}
