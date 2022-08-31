import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';

@Component({
  selector: 'kpn-monitor-route-properties-step-1-group',
  template: `
    <mat-form-field class="group">
      <mat-label>Group</mat-label>
      <mat-select [formControl]="group">
        <mat-option *ngFor="let gr of routeGroups" [value]="gr">
          {{ gr.groupName + ' - ' + gr.groupDescription }}
        </mat-option>
      </mat-select>
    </mat-form-field>

    <div class="kpn-button-group">
      <button mat-stroked-button matStepperNext>Next</button>
    </div>
  `,
  styles: [
    `
      .group {
        width: 20em;
      }
    `,
  ],
})
export class MonitorRoutePropertiesStep1GroupComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() group: FormControl<MonitorRouteGroup | null>;
  @Input() routeGroups: MonitorRouteGroup[];
}
