import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MonitorRouteDescriptionComponent } from './monitor-route-description.component';
import { MonitorRouteNameComponent } from './monitor-route-name.component';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'ui-monitor-route-properties-step-2-name',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <ui-monitor-route-name [ngForm]="ngForm()" [name]="name()" />
    <ui-monitor-route-description [ngForm]="ngForm()" [description]="description()" />
    <div class="kpn-button-group">
      @if (mode() === 'update') {
        <button id="step2-back" mat-stroked-button matStepperPrevious i18n="@@action.back">
          Back
        </button>
      }

      <button id="step2-next" mat-stroked-button matStepperNext i18n="@@action.next">Next</button>
    </div>
  `,
  imports: [
    MatButtonModule,
    MatStepperModule,
    MonitorRouteDescriptionComponent,
    MonitorRouteNameComponent,
    ReactiveFormsModule,
  ],
})
export class MonitorRoutePropertiesStep2NameComponent {
  mode = input.required<string>();
  ngForm = input.required<FormGroupDirective>();
  name = input.required<FormControl<string>>();
  description = input.required<FormControl<string>>();
}
