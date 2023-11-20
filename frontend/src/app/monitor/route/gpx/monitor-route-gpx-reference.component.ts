import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { DataComponent } from '@app/components/shared/data';
import { DayInputComponent } from '@app/components/shared/format';
import { DistancePipe } from '@app/components/shared/format';
import { IconHappyComponent } from '@app/components/shared/icon';
import { MarkdownModule } from 'ngx-markdown';
import { MonitorRouteDetailsAnalysisComponent } from '../details/monitor-route-details-analysis.component';
import { MonitorRouteDetailsReferenceComponent } from '../details/monitor-route-details-reference.component';
import { MonitorRouteDetailsStructureComponent } from '../details/monitor-route-details-structure.component';
import { MonitorRouteDetailsSummaryComponent } from '../details/monitor-route-details-summary.component';

@Component({
  selector: 'kpn-monitor-route-gpx-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@monitor.route.properties.reference-details.file">
      Select the file that contains the GPX trace:
    </p>
    <div class="kpn-small-spacer-above">
      <input
        type="file"
        id="gpx-file-input"
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
        id="gpx-file-name"
        class="kpn-label"
        i18n="@@monitor.route.properties.reference-details.file.name"
      >File</span
      >
      {{ referenceFilename.value }}
    </div>

    <div class="kpn-form-error">
      <div *ngIf="referenceFile.errors?.maxFileSizeExceeded"
        i18n="@@monitor.route.properties.reference-details.file.max-size-exceeded">
        Cannot upload this file.  It is too big (maximum file size is {{referenceFile.errors.maxFileSizeExceeded}}).
      </div>
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
        id="reference-filename.required"
        i18n="@@monitor.route.reference-filename.required"
      >
        Reference filename is required
      </div>
    </div>

    <p i18n="@@monitor.route.properties.reference-details.gpx.reference-day">
      Select the date at which the gpx trace was recorded or was known to be
      valid:
    </p>
    <kpn-day-input
      id="gpx-reference-date"
      [date]="gpxReferenceDate"
      label="Reference day"
      i18n-label="@@monitor.route.properties.reference-details.day.label"
    />
    <div
      *ngIf="
        gpxReferenceDate.invalid &&
        (gpxReferenceDate.touched || ngForm.submitted)
      "
      class="kpn-form-error"
    >
      <div
        *ngIf="gpxReferenceDate.errors?.required"
        id="reference-day.required"
        i18n="@@monitor.route.reference-day.required"
      >
        Please provide a valid reference day
      </div>
    </div>
  `,
  styles: `
    .file-input {
      display: none;
    }
  `,
  standalone: true,
  imports: [
    DataComponent,
    DayInputComponent,
    DistancePipe,
    IconHappyComponent,
    MarkdownModule,
    MatButtonModule,
    MonitorRouteDetailsAnalysisComponent,
    MonitorRouteDetailsReferenceComponent,
    MonitorRouteDetailsStructureComponent,
    MonitorRouteDetailsSummaryComponent,
    NgIf,
    RouterLink,
  ],
})
export class MonitorRouteGpxReferenceComponent {
  @Input({ required: true }) ngForm: FormGroupDirective;
  @Input({ required: true }) gpxReferenceDate: FormControl<Date | null>;
  @Input({ required: true }) referenceFilename: FormControl<string>;
  @Input({ required: true }) referenceFile: FormControl<File>;

  selectFile(selectEvent: any) {
    this.referenceFile.setValue(selectEvent.target.files[0]);
    this.referenceFilename.setValue(selectEvent.target.files[0].name);
  }
}
