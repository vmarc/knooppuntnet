import { input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
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
  selector: 'ui-monitor-route-name',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <nz-form-item nz-row>
      <nz-form-label nzRequired nzFor="name" i18n="@@monitor.route.name.label">
        Name
      </nz-form-label>
      <nz-form-control nzHasFeedback [nzValidateStatus]="validateStatus()">
        <input nz-input id="name" [formControl]="name()" required />
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
    NzRowDirective,
    ReactiveFormsModule,
  ],
})
export class MonitorRouteNameComponent {
  readonly ngForm = input.required<FormGroupDirective>();
  readonly name = input.required<FormControl<string>>();

  validateStatus(): string {
    return FormUtil.validateStatus(this.ngForm(), this.name());
  }

  error(): string | null {
    const errors = this.name().errors;
    if (errors) {
      if (errors['required']) {
        return $localize`:@@monitor.route.name.required:Name is required.`;
      }
      if (errors['maxlength']) {
        const e = errors['maxlength'];
        return $localize`:@@monitor.route.name.maxlength:Too long (max= ${e.requiredLength}, actual=${e.actualLength}).`;
      }

      if (errors['routeNameNonUnique']) {
        return $localize`:@@monitor.route.name.unique:The route name should be unique within the group. A route with this name already exists within this group.`;
      }
    }
    return null;
  }
}
