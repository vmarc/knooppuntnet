import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteDetailsPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteDetailsPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-reference-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header
      pageName="reference"
    ></kpn-monitor-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">Route not found</div>

      <div *ngIf="response.result as route">
        <p>
          Select the type of reference to be used for route monitoring and
          analysis:
        </p>

        <div class="reference">
          <mat-radio-group
            [value]="referenceType"
            (change)="referenceTypeChanged($event)"
          >
            <mat-radio-button value="osm" title="OpenStreetMap">
              OpenStreetMap route relation
            </mat-radio-button>
            <mat-radio-button value="gpx" title="GPX">
              GPX trace
            </mat-radio-button>
          </mat-radio-group>
        </div>

        <div *ngIf="referenceType === 'osm'">
          <p>
            Select the date of the route relation state that will serve as a
            reference (default today):
          </p>

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

          <div class="section-body kpn-form-buttons">
            <button
              mat-raised-button
              (click)="updateReference(route.groupName, route.routeName)"
              type="button"
            >
              Update reference and analyze
            </button>
          </div>
        </div>

        <div *ngIf="referenceType === 'gpx'">
          <p>
            Please upload the GPX file that will serve as a reference for the
            analysis:
          </p>
          <div class="section-body kpn-form-buttons">
            <input
              type="file"
              class="file-input"
              (change)="selectFile(route.groupName, route.routeName, $event)"
              #fileInput
            />

            <button mat-raised-button (click)="fileInput.click()" type="button">
              Upload GPX and analyze
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .section-body {
        padding-left: 1em;
      }

      .reference {
        padding-bottom: 2em;
      }

      .reference mat-radio-button {
        display: block;
        padding-top: 0.5em;
        padding-bottom: 0.5em;
        padding-left: 1em;
      }

      .file-input {
        display: none;
      }
    `,
  ],
})
export class MonitorRouteReferencePageComponent implements OnInit {
  readonly response$ = this.store.select(selectMonitorRouteDetailsPage);

  referenceType = 'osm';
  progress = 0;
  message = '';

  constructor(
    private snackBar: MatSnackBar,
    private store: Store<AppState>,
    private monitorService: MonitorService
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteDetailsPageInit());
  }

  updateReference(groupName: string, routeName: string): void {
    this.snackBar.open('Not implemented yet', 'close', {
      panelClass: ['mat-toolbar', 'mat-primary'],
    });
  }

  gpxUpload(groupName: string, routeName: string): void {
    this.snackBar.open('Sorry, GPX file upload not implemented yet', 'close', {
      panelClass: ['mat-toolbar', 'mat-primary'],
    });
  }

  referenceTypeChanged(event: MatRadioChange): void {
    this.referenceType = event.value;
  }

  selectFile(groupName: string, routeName: string, selectEvent: any) {
    this.monitorService
      .routeGpxUpload(groupName, routeName, selectEvent.target.files[0])
      .subscribe();
  }
}
