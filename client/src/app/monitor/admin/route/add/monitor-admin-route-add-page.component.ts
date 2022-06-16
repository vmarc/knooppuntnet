import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { UntypedFormControl } from '@angular/forms';
import { UntypedFormGroup } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';
import { MonitorRouteAdd } from '@api/common/monitor/monitor-route-add';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { actionMonitorRouteAdd } from '../../../store/monitor.actions';
import { actionMonitorRouteInfo } from '../../../store/monitor.actions';
import { selectMonitorRouteInfoPage } from '../../../store/monitor.selectors';
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

    <h2>Add route</h2>

    <form [formGroup]="form">
      <div class="section-title">
        Please provide the OpenStreetMap relation id of the route that you want
        to add to the monitor:
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

      <div *ngIf="routeInfo$ | async as routeInfo">
        <kpn-monitor-admin-route-info
          [routeInfo]="routeInfo.result"
        ></kpn-monitor-admin-route-info>

        <p>
          <mat-form-field>
            <mat-label>Name</mat-label>
            <input matInput [formControl]="name" />
          </mat-form-field>
        </p>

        <p>
          <mat-form-field class="description">
            <mat-label>Description</mat-label>
            <input matInput [formControl]="description" />
          </mat-form-field>
        </p>

        <div class="kpn-form-buttons">
          <button mat-stroked-button (click)="save()">Save Route</button>
          <a [routerLink]="groupLink$ | async">Cancel</a>
        </div>
      </div>
    </form>
  `,
  styles: [
    `
      .section-title {
        padding-top: 2em;
      }

      .section-body {
        padding-top: 1em;
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
    `,
  ],
})
export class MonitorAdminRouteAddPageComponent {
  readonly routeInfo$ = this.store.select(selectMonitorRouteInfoPage);
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

  readonly routeId = new UntypedFormControl('', [Validators.required]);
  readonly name = new UntypedFormControl('', [Validators.required]);
  readonly description = new UntypedFormControl('', [Validators.required]);

  readonly form = new UntypedFormGroup({
    routeId: this.routeId,
    name: this.name,
    description: this.description,
  });

  constructor(private store: Store<AppState>) {}

  getRouteInformation(): void {
    this.store.dispatch(
      actionMonitorRouteInfo({ routeId: this.routeId.value })
    );
  }

  save(): void {
    const add: MonitorRouteAdd = this.form.value;
    this.store.dispatch(actionMonitorRouteAdd({ add }));
  }

  referenceTypeChanged(event: MatRadioChange): void {
    // this.store.dispatch(actionMonitorRouteMapMode({mode: event.value}));
  }

  gpxUpload(): void {}
}
