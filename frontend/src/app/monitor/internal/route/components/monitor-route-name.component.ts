import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
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
    <div>
      <nz-form-item>
        <nz-form-label nzRequired nzFor="name" i18n="@@monitor.route.name.label">
          Name
        </nz-form-label>
        <nz-form-control nzHasFeedback [nzValidateStatus]="validateStatus()">
          <input nz-input id="name" [formControl]="name" required />
        </nz-form-control>
        @if (validateStatus() === 'error') {
          <div class="ant-form-item-explain">
            @if (name.errors?.['required']) {
              <div class="ant-form-item-explain-error" i18n="@@monitor.route.name.required">
                Name is required.
              </div>
            }
            @if (name.errors?.['maxlength']) {
              <span class="ant-form-item-explain-error" i18n="@@monitor.route.name.maxlength">
                Too long (max= {{ name.errors['maxlength'].requiredLength }}, actual={{
                  name.errors?.['maxlength'].actualLength
                }}).
              </span>
            }
            @if (name.errors?.['routeNameNonUnique']) {
              <span class="ant-form-item-explain-error" i18n="@@monitor.route.name.unique">
                The route name should be unique within the group. A route with this name already
                exists within this group.
              </span>
            }
          </div>
        }
      </nz-form-item>
    </div>
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
export class MonitorRouteNameComponent {
  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly name = this.monitorForm.name;

  validateStatus(): string {
    return this.monitorForm.validateStatus(this.name);
  }
}
