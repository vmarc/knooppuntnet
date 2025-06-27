import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatStepperModule } from '@angular/material/stepper';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';

@Component({
  selector: 'ui-monitor-route-properties-step-1-group',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <form [formGroup]="groupForm" #ngGroupForm="ngForm">
      <mat-form-field class="group">
        <mat-label i18n="@@monitor.route.properties.group">Group</mat-label>
        <mat-select id="group-selector" [formControl]="group">
          @for (gr of routeGroups(); track $index) {
            <mat-option [value]="gr">
              {{ gr.groupName + ' - ' + gr.groupDescription }}
            </mat-option>
          }
        </mat-select>
      </mat-form-field>
    </form>

    <div class="kpn-button-group">
      <button id="step1-next" nz-button nzType="default" i18n="@@action.next">Next</button>
    </div>
  `,
  styles: `
    .group {
      width: 20em;
    }
  `,
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatOptionModule,
    MatSelectModule,
    MatStepperModule,
    ReactiveFormsModule,
    NzButtonComponent,
  ],
})
export class MonitorRoutePropertiesStep1GroupComponent {
  readonly routeGroups = input.required<MonitorRouteGroup[]>();
  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly groupForm = this.monitorForm.groupForm;
  protected readonly group = this.monitorForm.group;
}
