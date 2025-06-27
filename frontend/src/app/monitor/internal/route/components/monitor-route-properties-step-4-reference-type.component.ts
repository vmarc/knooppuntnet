import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatRadioModule } from '@angular/material/radio';
import { MatStepperModule } from '@angular/material/stepper';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';

@Component({
  selector: 'ui-monitor-route-properties-step-4-reference-type',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <form [formGroup]="form" #ngForm="ngForm">
      <div class="question">
        <p i18n="@@monitor.route.properties.reference-type.question">
          What do you want to use as reference to compare the OSM relation to?
        </p>
        <mat-radio-group [formControl]="referenceType">
          <mat-radio-button id="reference-type.gpx" class="answer" value="gpx">
            <span i18n="@@monitor.route.properties.reference-type.gpx">
              A GPX trace that you will upload now
            </span>
          </mat-radio-button>
          <mat-radio-button id="reference-type.multi-gpx" class="answer" value="multi-gpx">
            <span i18n="@@monitor.route.properties.reference-type.multi-gpx">
              Multiple GPX traces (one per route in super route), to be uploaded separately
            </span>
          </mat-radio-button>
          <mat-radio-button id="reference-type.osm-now" class="answer" value="osm-now">
            <span i18n="@@monitor.route.properties.reference-type.osm-now">
              The OSM relation at this moment
            </span>
          </mat-radio-button>
          <mat-radio-button id="reference-type.osm-past" class="answer" value="osm-past">
            <span i18n="@@monitor.route.properties.reference-type.osm-past">
              The OSM relation at a given moment in the past
            </span>
          </mat-radio-button>
        </mat-radio-group>
      </div>

      @if (
        referenceType.invalid &&
        referenceType.errors &&
        (referenceType.dirty || referenceType.touched || ngForm.submitted)
      ) {
        @if (referenceType.errors['required']) {
          <p
            id="reference-type.required"
            class="kpn-warning"
            i18n="@@monitor.route.properties.reference-type.required"
          >
            Please answer the question
          </p>
        }
      }
    </form>

    <div class="kpn-button-group">
      <button id="step4-back" nz-button nzType="default" i18n="@@action.back">Back</button>
      <button id="step4-next" nz-button nzType="default" i18n="@@action.next">Next</button>
    </div>
  `,
  styles: `
    .question {
      padding-bottom: 1em;
    }

    .answer {
      display: block;
      padding-top: 0.5em;
    }
  `,
  imports: [
    MatButtonModule,
    MatRadioModule,
    MatStepperModule,
    ReactiveFormsModule,
    NzButtonComponent,
  ],
})
export class MonitorRoutePropertiesStep4ReferenceTypeComponent {
  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly form = this.monitorForm.referenceTypeForm;
  protected readonly referenceType = this.monitorForm.referenceType;
}
