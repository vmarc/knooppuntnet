import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-route-properties-step-5-reference-details',
  template: `
    <div [ngClass]="{ hidden: referenceType.value !== 'osm' }">
      <p i18n="@@monitor.route.properties.reference-details.date">
        Select the date of the route relation state that will serve as a
        reference (default today):
      </p>
      <kpn-day-input
        [ngForm]="ngForm"
        [date]="osmReferenceDate"
        label="Reference date"
      >
      </kpn-day-input>
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
        {{ gpxFilename.value }}
      </div>
    </div>

    <div class="kpn-button-group">
      <button mat-stroked-button matStepperPrevious i18n="@@action.back">
        Back
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
  @Input() osmReferenceDate: FormControl<Date | null>;
  @Input() gpxFilename: FormControl<string>;
  @Input() gpxFile: FormControl<File>;

  selectFile(selectEvent: any) {
    this.gpxFile.setValue(selectEvent.target.files[0]);
    this.gpxFilename.setValue(selectEvent.target.files[0].name);
  }
}
