import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzFormControlComponent } from 'ng-zorro-antd/form';
import { NzRowDirective } from 'ng-zorro-antd/grid';
import { NzColDirective } from 'ng-zorro-antd/grid';
import { NzSelectComponent } from 'ng-zorro-antd/select';
import { NzOptionComponent } from 'ng-zorro-antd/select';

@Component({
  selector: 'ui-monitor-route-group',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <nz-form-item>
      <nz-form-label nzRequired nzFor="group-selector" i18n="@@monitor.route.properties.group">
        Group
      </nz-form-label>
      <nz-form-control>
        <nz-select id="group-selector" [formControl]="group">
          @for (routeGroup of routeGroups(); track routeGroup.groupName) {
            <nz-option [nzValue]="routeGroup" [nzLabel]="groupLabel(routeGroup)" />
          }
        </nz-select>
      </nz-form-control>
    </nz-form-item>
  `,
  imports: [
    NzColDirective,
    NzFormControlComponent,
    NzFormItemComponent,
    NzFormLabelComponent,
    NzOptionComponent,
    NzRowDirective,
    NzSelectComponent,
    ReactiveFormsModule,
  ],
})
export class MonitorRouteGroupComponent {
  readonly routeGroups = input.required<MonitorRouteGroup[]>();

  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly group = this.monitorForm.group;

  groupLabel(group: MonitorRouteGroup): string {
    return group.groupName + ' - ' + group.groupDescription;
  }
}
