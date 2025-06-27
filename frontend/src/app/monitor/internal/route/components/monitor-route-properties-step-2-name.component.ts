import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { MonitorRouteDescriptionComponent } from './monitor-route-description.component';
import { MonitorRouteNameComponent } from './monitor-route-name.component';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'ui-monitor-route-properties-step-2-name',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <form [formGroup]="form" #ngForm="ngForm">
      <ui-monitor-route-name [ngForm]="ngForm" [name]="name" />
      <ui-monitor-route-description [ngForm]="ngForm" [description]="description" />
    </form>

    <div class="kpn-button-group">
      @if (mode() === 'update') {
        <button id="step2-back" nz-button nzType="default" i18n="@@action.back">Back</button>
      }

      <button id="step2-next" nz-button nzType="default" i18n="@@action.next">Next</button>
    </div>
  `,
  imports: [
    MonitorRouteDescriptionComponent,
    MonitorRouteNameComponent,
    ReactiveFormsModule,
    NzButtonComponent,
  ],
})
export class MonitorRoutePropertiesStep2NameComponent {
  readonly mode = input.required<string>();

  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly form = this.monitorForm.nameForm;
  protected readonly name = this.monitorForm.name;
  protected readonly description = this.monitorForm.description;
}
