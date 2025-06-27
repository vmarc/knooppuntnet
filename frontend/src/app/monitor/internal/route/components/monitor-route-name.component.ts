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
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <nz-form-item>
        <nz-form-label nzRequired nzFor="name" i18n="@@monitor.route.name.label">
          Name
        </nz-form-label>
        <nz-form-control nzHasFeedback [nzErrorTip]="nameError">
          <input nz-input id="name" [formControl]="name" required />
          <ng-template #nameError let-name>
            @if (name.errors?.['required']) {
              <div i18n="@@monitor.route.name.required">Name is required.</div>
            }
            @if (name.errors?.['maxlength']) {
              <span i18n="@@monitor.route.name.maxlength">
                Too long (max= {{ name.errors['maxlength'].requiredLength }}, actual={{
                  name.errors?.['maxlength'].actualLength
                }}).
              </span>
            }
            @if (name.errors?.['routeNameNonUnique']) {
              <span i18n="@@monitor.route.name.unique">
                The route name should be unique within the group. A route with this name already
                exists within this group.
              </span>
            }
          </ng-template>
        </nz-form-control>
      </nz-form-item>
    </div>
    <pre>{{ debug() }}</pre>
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

  debug(): string {
    return (
      'invalid=' +
      this.name.invalid +
      ', errors=' +
      JSON.stringify(this.name.errors) +
      ', value=' +
      JSON.stringify(this.name.value) +
      ', valid=' +
      this.name.valid +
      ', pristine=' +
      this.name.pristine +
      ', status=' +
      this.name.status +
      ', dirty=' +
      this.name.dirty +
      ', touched=' +
      this.name.touched
    ); // || ngForm().submitted)
  }
}
