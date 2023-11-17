import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatRadioModule } from '@angular/material/radio';
import { MatStepperModule } from '@angular/material/stepper';
import { FormStatusComponent } from '@app/components/shared';

@Component({
  selector: 'kpn-monitor-route-properties-step-4-reference-type',
  template: `
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
        <mat-radio-button
          id="reference-type.multi-gpx"
          class="answer"
          value="multi-gpx"
        >
          <span i18n="@@monitor.route.properties.reference-type.multi-gpx">
            Multiple GPX traces (one per route in super route), to be uploaded
            separately
          </span>
        </mat-radio-button>
        <mat-radio-button
          id="reference-type.osm-now"
          class="answer"
          value="osm-now"
        >
          <span i18n="@@monitor.route.properties.reference-type.osm-now">
            The OSM relation at this moment
          </span>
        </mat-radio-button>
        <mat-radio-button
          id="reference-type.osm-past"
          class="answer"
          value="osm-past"
        >
          <span i18n="@@monitor.route.properties.reference-type.osm-past">
            The OSM relation at a given moment in the past
          </span>
        </mat-radio-button>
      </mat-radio-group>
    </div>
    <div
      *ngIf="
        referenceType.invalid &&
        (referenceType.dirty || referenceType.touched || ngForm.submitted)
      "
      class="kpn-warning"
    >
      <p
        *ngIf="referenceType.errors.required"
        id="reference-type.required"
        i18n="@@monitor.route.properties.reference-type.required"
      >
        Please answer the question
      </p>
    </div>

    <kpn-form-status
      formName="step4-form"
      [statusChanges]="ngForm.statusChanges"
    ></kpn-form-status>
    <div class="kpn-button-group">
      <button
        id="step4-back"
        mat-stroked-button
        matStepperPrevious
        i18n="@@action.back"
      >
        Back
      </button>
      <button
        id="step4-next"
        mat-stroked-button
        matStepperNext
        i18n="@@action.next"
      >
        Next
      </button>
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
  standalone: true,
  imports: [
    FormStatusComponent,
    MatButtonModule,
    MatRadioModule,
    MatStepperModule,
    NgIf,
    ReactiveFormsModule,
  ],
})
export class MonitorRoutePropertiesStep4ReferenceTypeComponent {
  @Input({ required: true }) ngForm: FormGroupDirective;
  @Input({ required: true }) referenceType: FormControl<string>;
}
