import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzFormControlComponent } from 'ng-zorro-antd/form';
import { NzRowDirective } from 'ng-zorro-antd/grid';
import { NzColDirective } from 'ng-zorro-antd/grid';
import { NzInputDirective } from 'ng-zorro-antd/input';

@Component({
  selector: 'ui-monitor-route-description',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <nz-form-item>
      <nz-form-label nzRequired nzFor="description" i18n="@@monitor.route.description.label">
        Description
      </nz-form-label>
      <nz-form-control nzHasFeedback [nzValidateStatus]="validateStatus()">
        <input nz-input id="description" [formControl]="description" required />
      </nz-form-control>
      @if (validateStatus() === 'error') {
        <div class="ant-form-item-explain">
          @if (description.errors?.['required']) {
            <span class="ant-form-item-explain-error" i18n="@@monitor.route.description.required"
              >Description is required.</span
            >
          }
          @if (description.errors?.['maxlength']) {
            <span class="ant-form-item-explain-error" i18n="@@monitor.route.description.maxlength">
              Too long (max=
              {{ description.errors?.['maxlength'].requiredLength }}, actual={{
                description.errors?.['maxlength'].actualLength
              }}).
            </span>
          }
        </div>
      }
    </nz-form-item>
  `,
  imports: [
    NzColDirective,
    NzFormControlComponent,
    NzFormItemComponent,
    NzFormLabelComponent,
    NzInputDirective,
    NzRowDirective,
    ReactiveFormsModule,
  ],
})
export class MonitorRouteDescriptionComponent {
  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly description = this.monitorForm.description;

  validateStatus(): string {
    return this.monitorForm.validateStatus(this.description);
  }
}
