import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { FormStatusComponent } from '@app/components/shared';
import { MonitorRouteDescriptionComponent } from './monitor-route-description.component';
import { MonitorRouteNameComponent } from './monitor-route-name.component';

@Component({
  selector: 'kpn-monitor-route-properties-step-2-name',
  template: `
    <kpn-monitor-route-name [ngForm]="ngForm" [name]="name" />
    <kpn-monitor-route-description [ngForm]="ngForm" [description]="description" />
    <kpn-form-status formName="step2-form" [statusChanges]="ngForm.statusChanges"></kpn-form-status>
    <div class="kpn-button-group">
      @if (mode === 'update') {
        <button id="step2-back" mat-stroked-button matStepperPrevious i18n="@@action.back">
          Back
        </button>
      }

      <button id="step2-next" mat-stroked-button matStepperNext i18n="@@action.next">Next</button>
    </div>
  `,
  standalone: true,
  imports: [
    FormStatusComponent,
    MatButtonModule,
    MatStepperModule,
    MonitorRouteDescriptionComponent,
    MonitorRouteNameComponent,
    ReactiveFormsModule,
  ],
})
export class MonitorRoutePropertiesStep2NameComponent {
  @Input({ required: true }) mode: string;
  @Input({ required: true }) ngForm: FormGroupDirective;
  @Input({ required: true }) name: FormControl<string>;
  @Input({ required: true }) description: FormControl<string>;
}
