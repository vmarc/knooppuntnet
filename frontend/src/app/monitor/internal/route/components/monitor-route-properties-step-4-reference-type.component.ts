import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';

@Component({
  selector: 'ui-monitor-route-properties-step-4-reference-type',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <form [formGroup]="form" #ngForm="ngForm">
      <p i18n="@@monitor.route.properties.reference-type.question">
        What do you want to use as reference to compare the OSM relation to?
      </p>
      <nz-radio-group [formControl]="referenceType">
        <label
          nz-radio
          id="reference-type.gpx"
          class="answer"
          nzValue="gpx"
          i18n="@@monitor.route.properties.reference-type.gpx"
        >
          A GPX trace that you will upload now
        </label>
        <label
          nz-radio
          id="reference-type.multi-gpx"
          class="answer"
          nzValue="multi-gpx"
          i18n="@@monitor.route.properties.reference-type.multi-gpx"
        >
          Multiple GPX traces (one per route in super route), to be uploaded separately
        </label>
        <label
          nz-radio
          id="reference-type.osm-now"
          class="answer"
          nzValue="osm-now"
          i18n="@@monitor.route.properties.reference-type.osm-now"
        >
          The OSM relation at this moment
        </label>
        <label
          nz-radio
          id="reference-type.osm"
          class="answer"
          nzValue="osm"
          i18n="@@monitor.route.properties.reference-type.osm"
        >
          The OSM relation at a given moment in the past
        </label>
      </nz-radio-group>

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
  imports: [NzRadioComponent, NzRadioGroupComponent, ReactiveFormsModule],
})
export class MonitorRoutePropertiesStep4ReferenceTypeComponent {
  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly form = this.monitorForm.referenceTypeForm;
  protected readonly referenceType = this.monitorForm.referenceType;
}
