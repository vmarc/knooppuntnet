import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-route-properties-step-5-reference-details',
  template: `
    <div [ngClass]="{ hidden: referenceType.value !== 'osm' }">
      <p i18n="@@monitor.route.properties.reference-details.day">
        Select the date of the route relation state that will serve as a
        reference (default today):
      </p>
      <kpn-day-input
        [date]="osmReferenceDate"
        label="Reference day"
        i18n-label="@@monitor.route.properties.reference-details.day.label"
      >
      </kpn-day-input>
      <div
        *ngIf="
          osmReferenceDate.invalid &&
          (osmReferenceDate.touched || ngForm.submitted)
        "
        class="kpn-form-error"
      >
        <div
          *ngIf="osmReferenceDate.errors?.required"
          i18n="@@monitor.route.reference-day.required"
        >
          Please provide a valid reference day
        </div>
      </div>
    </div>

    <div [ngClass]="{ hidden: referenceType.value !== 'gpx' }">
      <p i18n="@@monitor.route.properties.reference-details.file">
        Select the file that contains the GPX trace:
      </p>
      <div class="kpn-small-spacer-above">
        <input
          type="file"
          class="file-input"
          (change)="selectFile($event)"
          #fileInput
        />
        <button
          mat-stroked-button
          (click)="fileInput.click()"
          type="button"
          i18n="@@monitor.route.properties.reference-details.file.select"
        >
          Select file
        </button>
      </div>
      <div class="kpn-small-spacer-above kpn-spacer-below">
        <span
          class="kpn-label"
          i18n="@@monitor.route.properties.reference-details.file.name"
          >File</span
        >
        {{ referenceFilename.value }}
      </div>
      <div
        *ngIf="
          referenceFilename.invalid &&
          (referenceFilename.dirty ||
            referenceFilename.touched ||
            ngForm.submitted)
        "
        class="kpn-form-error"
      >
        <div
          *ngIf="referenceFilename.errors?.required"
          i18n="@@monitor.route.reference-filename.required"
        >
          Reference filename is required
        </div>
      </div>

      <p i18n="@@monitor.route.properties.reference-details.gpx.reference-day">
        Select the date at which the gpx trace was recorded or was known to be
        valid (default today):
      </p>
      <kpn-day-input
        [date]="gpxReferenceDate"
        label="Reference day"
        i18n-label="@@monitor.route.properties.reference-details.day.label"
      >
      </kpn-day-input>
      <div
        *ngIf="
          gpxReferenceDate.invalid &&
          (gpxReferenceDate.touched || ngForm.submitted)
        "
        class="kpn-form-error"
      >
        <div
          *ngIf="gpxReferenceDate.errors?.required"
          i18n="@@monitor.route.reference-day.required"
        >
          Please provide a valid reference day
        </div>
      </div>
    </div>

    <div class="kpn-button-group">
      <button mat-stroked-button matStepperPrevious i18n="@@action.back">
        Back
      </button>
      <button mat-stroked-button matStepperNext i18n="@@action.next">
        Next
      </button>
    </div>
  `,
  styles: [
    `
      .file-input {
        display: none;
      }
    `,
  ],
})
export class MonitorRoutePropertiesStep5ReferenceDetailsComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() referenceType: FormControl<string>;
  @Input() referenceComment: FormControl<string>;
  @Input() osmReferenceDate: FormControl<Date | null>;
  @Input() gpxReferenceDate: FormControl<Date | null>;
  @Input() referenceFilename: FormControl<string>;
  @Input() referenceFile: FormControl<File>;

  selectFile(selectEvent: any) {
    this.referenceFile.setValue(selectEvent.target.files[0]);
    this.referenceFilename.setValue(selectEvent.target.files[0].name);
  }
}
