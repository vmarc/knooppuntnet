import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Validators} from '@angular/forms';
import {FormControl} from '@angular/forms';
import {FormGroup} from '@angular/forms';
import {MatRadioChange} from '@angular/material/radio/radio';
import {of} from 'rxjs';

@Component({
  selector: 'kpn-monitor-admin-route-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li><a [routerLink]="groupLink$ | async">{{groupDescription$ | async}}</a></li>
      <li>Route</li>
    </ul>

    <h1>
      {{groupDescription$ | async}}
    </h1>

    <kpn-page-menu>
      <span>
        Add route
      </span>
    </kpn-page-menu>

    <form [formGroup]="form">
      <div class="section-title">
        Please provide the OpenStreetMap relation id of the route that you want to add to the monitor.
      </div>
      <div class="section-body">
        <mat-form-field>
          <mat-label>Route relation id</mat-label>
          <input matInput [formControl]="routeId">
        </mat-form-field>
        <div>
          <button
            mat-stroked-button
            type="button"
            (click)="getRouteInformation()">
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
  styles: [`

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
  `]
})
export class MonitorAdminRouteAddPageComponent {

  readonly groupDescription$ = of('Group One');
  readonly groupLink$ = of('/monitor/groups/group-1');
  readonly routeId = new FormControl('', [Validators.required]);

  readonly form = new FormGroup({
    routeId: this.routeId
  });

  getRouteInformation(): void {
  }

  save(): void {
  }

  referenceTypeChanged(event: MatRadioChange): void {
    // this.store.dispatch(actionMonitorRouteMapMode({mode: event.value}));
  }

  gpxUpload(): void {
  }
}
