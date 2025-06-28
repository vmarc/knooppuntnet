import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { ChangeDetectionStrategy } from '@angular/core';
import { FormErrorComponent } from '@app/shared/components/form/form-error.component';
import { FormUtil } from '@app/shared/form/form-util';
import { NzFormControlComponent } from 'ng-zorro-antd/form';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzRowDirective } from 'ng-zorro-antd/grid';
import { NzColDirective } from 'ng-zorro-antd/grid';
import { NzInputDirective } from 'ng-zorro-antd/input';

@Component({
  selector: 'ui-monitor-group-description',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <nz-form-item nz-row>
      <nz-form-label nzRequired nzFor="description" i18n="@@monitor.group.description.label">
        Description
      </nz-form-label>
      <nz-form-control nzHasFeedback [nzValidateStatus]="validateStatus()">
        <input nz-input id="description" [formControl]="description()" required />
      </nz-form-control>
      @if (validateStatus() === 'error') {
        <ui-form-error [error]="error()" />
      }
    </nz-form-item>
  `,
  imports: [
    FormErrorComponent,
    NzColDirective,
    NzFormControlComponent,
    NzFormItemComponent,
    NzFormLabelComponent,
    NzInputDirective,
    ReactiveFormsModule,
    NzRowDirective,
  ],
})
export class MonitorGroupDescriptionComponent {
  readonly ngForm = input.required<FormGroupDirective>();
  readonly description = input.required<FormControl<string>>();

  validateStatus(): string {
    return FormUtil.validateStatus(this.ngForm(), this.description());
  }

  error(): string {
    const errors = this.description().errors;
    if (errors) {
      if (errors['required']) {
        return $localize`:@@monitor.group.description.required:Description is required.`;
      }
      if (errors['maxlength']) {
        const e = errors['maxlength'];
        return $localize`:@@monitor.group.description.maxlength:Too long (max= ${e.requiredLength}, actual=${e.actualLength}).`;
      }
    }
    return null;
  }
}
