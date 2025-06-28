import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';
import { MonitorRouteDescriptionComponent } from '@app/monitor/internal/route/components/monitor-route-description.component';
import { MonitorRouteGroupComponent } from '@app/monitor/internal/route/components/monitor-route-group.component';
import { MonitorRouteNameComponent } from '@app/monitor/internal/route/components/monitor-route-name.component';
import { MonitorRouteForm } from './monitor-route-form.service';
import { NzFormDirective } from 'ng-zorro-antd/form';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'ui-monitor-route-properties-step-2-name',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <form nz-form nzLayout="vertical" [formGroup]="form" #ngForm="ngForm">
      @if (mode() === 'update') {
        <ui-monitor-route-group [routeGroups]="routeGroups()" />
      }
      <ui-monitor-route-name [ngForm]="ngForm" [name]="name" />
      <ui-monitor-route-description [ngForm]="ngForm" [description]="description" />
    </form>
  `,
  imports: [
    MonitorRouteDescriptionComponent,
    MonitorRouteGroupComponent,
    MonitorRouteNameComponent,
    NzFormDirective,
    ReactiveFormsModule,
  ],
})
export class MonitorRoutePropertiesStep2NameComponent {
  readonly mode = input.required<string>();
  readonly routeGroups = input.required<MonitorRouteGroup[]>();

  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly form = this.monitorForm.nameForm;
  protected readonly name = this.monitorForm.name;
  protected readonly description = this.monitorForm.description;
}
