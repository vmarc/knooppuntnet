import { NgClass } from '@angular/common';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { DayInputComponent } from '@app/shared/components/format/day-input.component';
import { TimestampPipe } from '@app/shared/components/format/timestamp-pipe';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzDatePickerComponent } from 'ng-zorro-antd/date-picker';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzFormControlComponent } from 'ng-zorro-antd/form';

@Component({
  selector: 'ui-monitor-route-properties-step-5-reference-details',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    <form [formGroup]="form" #ngForm="ngForm">
      <div [ngClass]="{ hidden: referenceType.value !== 'osm-now' }">
        <p i18n="@@monitor.route.properties.reference-details.osm-now">
          The state of the OSM relation at this moment will be used as the reference for the monitor
          analysis.
        </p>
        <p i18n="@@monitor.route.properties.reference-details.osm-now.next-step">
          Continue with next step.
        </p>
      </div>

      <div [ngClass]="{ hidden: referenceType.value !== 'osm' }">
        @if (oldReferenceTimestamp) {
          <p class="kpn-spacer-below">
            <span
              class="kpn-label"
              i18n="@@monitor.route.properties.reference-details.osm-now.timestamp"
              >Current reference timestamp</span
            >
            {{ oldReferenceTimestamp | yyyymmddhhmm }}
          </p>
        }

        <nz-form-item nzHasFeedback>
          <nz-form-label nzRequired i18n="@@monitor.route.properties.reference-details.day">
            Select the date (midnight) of the route relation state that will serve as a reference
          </nz-form-label>
          <nz-form-control>
            <nz-date-picker id="osm-reference-date" [formControl]="osmReferenceDate" />
            <ng-template #descriptionError let-osmReferenceDate>
              @if (osmReferenceDate.errors['required']) {
                <span
                  id="osm-reference-date-required-error"
                  i18n="@@monitor.route.reference-day.required"
                >
                  Please provide a valid reference day
                </span>
              }
            </ng-template>
          </nz-form-control>
        </nz-form-item>

        <!--        @if (-->
        <!--          osmReferenceDate.invalid &&-->
        <!--          osmReferenceDate.errors &&-->
        <!--          (osmReferenceDate.touched || ngForm.submitted)-->
        <!--        ) {-->
        <!--          <div class="ant-form-item-explain">-->
        <!--            @if (osmReferenceDate.errors['required']) {-->
        <!--              <div-->
        <!--                id="osm-reference-date-required-error"-->
        <!--                i18n="@@monitor.route.reference-day.required"-->
        <!--              >-->
        <!--                Please provide a valid reference day-->
        <!--              </div>-->
        <!--            }-->
        <!--          </div>-->
        <!--        }-->
      </div>

      <div [ngClass]="{ hidden: referenceType.value !== 'gpx' }">
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
            nz-button
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

        @if (
          referenceFilename.invalid &&
          referenceFilename.errors &&
          (referenceFilename.dirty || referenceFilename.touched || ngForm.submitted)
        ) {
          <div class="ant-form-item-explain">
            @if (referenceFilename.errors['required']) {
              <div
                id="reference-filename.required"
                i18n="@@monitor.route.reference-filename.required"
              >
                Reference filename is required
              </div>
            }
          </div>
        }

        <p i18n="@@monitor.route.properties.reference-details.gpx.reference-day">
          Select the date at which the gpx trace was recorded or was known to be valid:
        </p>
        <ui-day-input
          id="gpx-reference-date"
          [date]="gpxReferenceDate"
          label="Reference day"
          i18n-label="@@monitor.route.properties.reference-details.day.label"
        />

        @if (
          gpxReferenceDate.invalid &&
          gpxReferenceDate.errors &&
          (gpxReferenceDate.touched || ngForm.submitted)
        ) {
          <div class="ant-form-item-explain">
            @if (gpxReferenceDate.errors['required']) {
              <div id="reference-day.required" i18n="@@monitor.route.reference-day.required">
                Please provide a valid reference day
              </div>
            }
          </div>
        }
      </div>

      <div id="multi-gpx.comment" [ngClass]="{ hidden: referenceType.value !== 'multi-gpx' }">
        <p i18n="@@monitor.route.properties.reference-details.multi-gpx.comment.1">
          Further reference details can be provided later.
        </p>
        <p i18n="@@monitor.route.properties.reference-details.multi-gpx.comment.2">
          The GPX traces per route in the superroute can be uploaded from the route details page
          after saving this route definition.
        </p>
        <p i18n="@@monitor.route.properties.reference-details.multi-gpx.next-step">
          Continue with next step.
        </p>
      </div>
    </form>
  `,
  styles: `
    .file-input {
      display: none;
    }
  `,
  imports: [
    NgClass,
    NzButtonComponent,
    NzDatePickerComponent,
    NzFormControlComponent,
    NzFormItemComponent,
    NzFormLabelComponent,
    ReactiveFormsModule,
    TimestampPipe,
    DayInputComponent,
  ],
})
export class MonitorRoutePropertiesStep5ReferenceDetailsComponent {
  private readonly monitorForm = inject(MonitorRouteForm);
  readonly form = this.monitorForm.referenceDetailsForm;
  readonly referenceType = this.monitorForm.referenceType;
  readonly osmReferenceDate = this.monitorForm.osmReferenceDate;
  readonly gpxReferenceDate = this.monitorForm.gpxReferenceDate;
  readonly referenceFilename = this.monitorForm.referenceFilename;
  readonly referenceFile = this.monitorForm.referenceFile;
  readonly oldReferenceTimestamp = this.monitorForm.oldReferenceTimestamp;

  selectFile(selectEvent: any) {
    if (selectEvent.target.files && selectEvent.target.files.length > 0) {
      this.referenceFile.setValue(selectEvent.target.files[0]);
      this.referenceFilename.setValue(selectEvent.target.files[0].name);
    }
  }
}
