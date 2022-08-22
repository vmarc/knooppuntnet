import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';

@Component({
  selector: 'kpn-monitor-route-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="section-title">
      Reference to be used for route monitoring and analysis:
    </div>

    <div class="section-body">
      <mat-radio-group [value]="'osm'" (change)="referenceTypeChanged($event)">
        <mat-radio-button value="osm" title="OpenStreetMap">
          OpenStreetMap route relation
        </mat-radio-button>

        <div class="section-body">
          <mat-form-field appearance="fill">
            <mat-label>Reference date</mat-label>
            <input matInput [matDatepicker]="picker" />
            <mat-datepicker-toggle
              matSuffix
              [for]="picker"
            ></mat-datepicker-toggle>
            <mat-datepicker #picker></mat-datepicker>
          </mat-form-field>
        </div>

        <mat-radio-button value="gpx" title="GPX"> GPX trace </mat-radio-button>

        <div class="section-body">
          <button mat-raised-button (click)="gpxUpload()" type="button">
            Upload GPX file
          </button>
        </div>
      </mat-radio-group>
    </div>
  `,
  styles: [
    `
      .section-title {
        padding-top: 2em;
      }

      .section-body {
        padding-left: 2em;
      }

      .section-body mat-radio-button {
        padding-top: 1em;
        padding-bottom: 1em;
      }

      .reference mat-radio-button {
        display: block;
        padding-top: 0.5em;
        padding-bottom: 0.5em;
        padding-left: 1em;
      }

      .kpn-button-group {
        padding-top: 2em;
      }
    `,
  ],
})
export class MonitorRouteReferenceComponent {
  referenceTypeChanged(event: MatRadioChange): void {
    // this.store.dispatch(actionMonitorRouteMapMode({mode: event.value}));
  }

  gpxUpload(): void {}
}
