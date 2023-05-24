import { NgFor } from '@angular/common';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatStepperModule } from '@angular/material/stepper';
import { MonitorRouteGroup } from '@api/common/monitor';

@Component({
  selector: 'kpn-monitor-route-properties-step-1-group',
  template: `
    <mat-form-field class="group">
      <mat-label i18n="@@monitor.route.properties.group">Group</mat-label>
      <mat-select id="group-selector" [formControl]="group">
        <mat-option *ngFor="let gr of routeGroups" [value]="gr">
          {{ gr.groupName + ' - ' + gr.groupDescription }}
        </mat-option>
      </mat-select>
    </mat-form-field>

    <div class="kpn-button-group">
      <button
        id="step1-next"
        mat-stroked-button
        matStepperNext
        i18n="@@action.next"
      >
        Next
      </button>
    </div>
  `,
  styles: [
    `
      .group {
        width: 20em;
      }
    `,
  ],
  standalone: true,
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatOptionModule,
    MatSelectModule,
    MatStepperModule,
    NgFor,
    ReactiveFormsModule,
  ],
})
export class MonitorRoutePropertiesStep1GroupComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() group: FormControl<MonitorRouteGroup | null>;
  @Input() routeGroups: MonitorRouteGroup[];
}
