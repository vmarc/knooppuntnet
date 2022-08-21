import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { Subscriptions } from '../../../../util/Subscriptions';
import { MonitorService } from '../../../monitor.service';
import { actionMonitorRouteAddPageInit } from '../../../store/monitor.actions';
import { actionMonitorRouteAdd } from '../../../store/monitor.actions';
import { actionMonitorRouteInfo } from '../../../store/monitor.actions';
import { selectMonitorRouteAddPage } from '../../../store/monitor.selectors';
import { selectMonitorRouteInfoPage } from '../../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../../store/monitor.selectors';
import { urlFragmentValidator } from '../../../validator/url-fragment-validator';

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
          <input matInput [formControl]="relationId" />
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
export class MonitorAdminRouteAddPageComponent implements OnInit, OnDestroy {
  private groupId = '';
  readonly response$ = this.store.select(selectMonitorRouteAddPage);
  readonly routeInfo$ = this.store.select(selectMonitorRouteInfoPage);
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

  readonly relationId = new FormControl<string>('', [Validators.required]);
  readonly name = new FormControl<string>('', {
    validators: [
      Validators.required,
      urlFragmentValidator,
      Validators.maxLength(15),
    ],
    asyncValidators: this.monitorService.asyncRouteNameUniqueValidator(
      () => this.groupId,
      ''
    ),
  });
  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);

  readonly form = new FormGroup({
    name: this.name,
    description: this.description,
    relationId: this.relationId,
  });

  private readonly subscriptions = new Subscriptions();

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteAddPageInit());
    this.subscriptions.add(
      this.store.select(selectMonitorRouteAddPage).subscribe((response) => {
        this.groupId = response?.result?.groupId;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  getRouteInformation(): void {
    this.store.dispatch(
      actionMonitorRouteInfo({ relationId: this.relationId.value })
    );
  }

  save(): void {
    this.store.dispatch(
      actionMonitorRouteAdd({ groupId: this.groupId, add: this.form.value })
    );
  }

  referenceTypeChanged(event: MatRadioChange): void {
    // this.store.dispatch(actionMonitorRouteMapMode({mode: event.value}));
  }

  gpxUpload(): void {}
}
