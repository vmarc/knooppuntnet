import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-route-step-4-reference-details',
  template: `
    <div [ngClass]="{ hidden: referenceType.value !== 'osm' }">
      <mat-form-field appearance="fill">
        <mat-label>Reference date</mat-label>
        <input matInput [matDatepicker]="picker" />
        <mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
        <mat-datepicker #picker></mat-datepicker>
      </mat-form-field>
    </div>

    <div [ngClass]="{ hidden: referenceType.value !== 'gpx' }">
      <p>Select the file that contains the GPX trace:</p>
      <input
        type="file"
        class="file-input"
        (change)="selectFile($event)"
        #fileInput
      />
      <button mat-raised-button (click)="fileInput.click()" type="button">
        Select
      </button>
      <p><span class="kpn-label">File</span> {{ gpxFile?.value?.name }}</p>
    </div>

    <div class="kpn-button-group">
      <button mat-stroked-button matStepperPrevious>Back</button>
      <button mat-stroked-button matStepperNext>Next</button>
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
export class MonitorRouteStep4ReferenceDetailsComponent {
  @Input() referenceType: FormControl<string>;
  @Input() referenceTimestamp: FormControl<string>;
  @Input() gpxFilename: FormControl<string>;
  @Input() gpxFile: FormControl<File>;

  selectFile(selectEvent: any) {
    this.gpxFile.setValue(selectEvent.target.files[0]);
  }
}
