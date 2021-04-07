import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio/radio';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { selectMonitorGroupDescription } from '../../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-admin-route-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>
        <a [routerLink]="groupLink$ | async">{{ groupDescription$ | async }}</a>
      </li>
      <li>Route</li>
    </ul>

    <h1>
      {{ groupDescription$ | async }}
    </h1>

    <kpn-page-menu>
      <span> Add route </span>
    </kpn-page-menu>

    <form [formGroup]="form">
      <div class="section-title">
        Please provide the OpenStreetMap relation id of the route that you want
        to add to the monitor.
      </div>
      <div class="section-body">
        <mat-form-field>
          <mat-label>Route relation id</mat-label>
          <input matInput [formControl]="routeId" />
        </mat-form-field>
        <div>
          <button
            mat-stroked-button
            type="button"
            (click)="getRouteInformation()"
          >
            Get route information
          </button>
        </div>
      </div>

      <kpn-monitor-admin-route-summary></kpn-monitor-admin-route-summary>

      <kpn-monitor-admin-route-reference></kpn-monitor-admin-route-reference>

      <div class="kpn-button-group">
        <button mat-stroked-button (click)="save()">Save Route</button>
        <a [routerLink]="groupLink$ | async">Cancel</a>
      </div>
    </form>
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
        padding-top: 3em;
      }
    `,
  ],
})
export class MonitorAdminRouteAddPageComponent {
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );
  readonly routeId = new FormControl('', [Validators.required]);

  readonly form = new FormGroup({
    routeId: this.routeId,
  });

  constructor(private store: Store<AppState>) {}

  getRouteInformation(): void {}

  save(): void {}

  referenceTypeChanged(event: MatRadioChange): void {
    // this.store.dispatch(actionMonitorRouteMapMode({mode: event.value}));
  }

  gpxUpload(): void {}
}
