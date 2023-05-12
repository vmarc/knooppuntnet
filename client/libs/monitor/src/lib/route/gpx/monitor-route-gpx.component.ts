import { NgIf } from '@angular/common';
import { computed } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { actionMonitorRouteGpxPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteGpxPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteName } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { selectMonitorRouteGpxPage } from '../../store/monitor.selectors';
import { MonitorRouteGpxBreadcrumbComponent } from './monitor-route-gpx-breadcrumb.component';
import { MonitorRouteGpxReferenceComponent } from './monitor-route-gpx-reference.component';

@Component({
  selector: 'kpn-monitor-route-details-gpx',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-gpx-breadcrumb />

    <div *ngIf="apiResponse() as response">
      <div *ngIf="!response.result" i18n="@@monitor.route.gpx.not-found">
        Route not found
      </div>

      <div *ngIf="response.result as page">
        <h1>{{ page.subRelationDescription }}</h1>
        <h2>GPX reference</h2>

        <div class="gpx-form">
          <form [formGroup]="form" #ngForm="ngForm">
            <kpn-monitor-route-gpx-reference
              [ngForm]="ngForm"
              [gpxReferenceDate]="gpxReferenceDate"
              [referenceFilename]="referenceFilename"
              [referenceFile]="referenceFile"
            />
          </form>
        </div>

        <div class="kpn-button-group">
          <button
            mat-raised-button
            id="save"
            color="primary"
            (click)="save()"
            [disabled]="form.invalid"
            i18n="@@action.save"
          >
            Save
          </button>
          <a [routerLink]="routeLink()" id="cancel" i18n="@@action.cancel">
            Cancel
          </a>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .gpx-form {
        margin-top: 2rem;
        margin-left: 2rem;
        margin-bottom: 4rem;
      }
    `,
  ],
  standalone: true,
  imports: [
    NgIf,
    RouterLink,
    MonitorRouteGpxReferenceComponent,
    MatButtonModule,
    ReactiveFormsModule,
    MonitorRouteGpxBreadcrumbComponent,
  ],
})
export class MonitorRouteGpxComponent implements OnInit, OnDestroy {
  readonly groupName = this.store.selectSignal(selectMonitorGroupName);
  readonly routeName = this.store.selectSignal(selectMonitorRouteName);
  readonly routeLink = computed(
    () => `/monitor/groups/${this.groupName()}/routes/${this.routeName()}`
  );
  readonly apiResponse = this.store.selectSignal(selectMonitorRouteGpxPage);

  readonly gpxReferenceDate = new FormControl<Date | null>(
    null,
    Validators.required
  );
  readonly referenceFilename = new FormControl<string | null>(
    null,
    Validators.required
  );
  readonly referenceFile = new FormControl<File | null>(null);

  readonly form = new FormGroup({
    gpxReferenceDate: this.gpxReferenceDate,
    referenceFilename: this.referenceFilename,
    referenceFile: this.referenceFile,
  });

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteGpxPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteGpxPageDestroy());
  }

  save(): void {
    console.log('TODO save');
  }
}
