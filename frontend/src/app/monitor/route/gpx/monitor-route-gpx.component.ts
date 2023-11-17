import { NgForOf } from '@angular/common';
import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { TimestampUtil } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorRouteFormErrorsComponent } from '../components/monitor-route-form-errors.component';
import { MonitorRouteFormSaveStepComponent } from '../components/monitor-route-form-save-step.component';
import { MonitorRouteGpxBreadcrumbComponent } from './monitor-route-gpx-breadcrumb.component';
import { MonitorRouteGpxReferenceComponent } from './monitor-route-gpx-reference.component';
import { MonitorRouteGpxService } from './monitor-route-gpx.service';

@Component({
  selector: 'kpn-monitor-route-gpx',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page *ngIf="service.state() as state">
      <kpn-monitor-route-gpx-breadcrumb
        [groupName]="state.groupName"
        [groupLink]="state.groupLink"
        [routeName]="state.routeName"
        [routeLink]="state.routeLink"
      />

      <div *ngIf="state.response as response">
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

          <div *ngIf="busy() === false" class="kpn-button-group">
            <button
              mat-raised-button
              id="save"
              color="primary"
              (click)="save()"
              [disabled]="form.invalid"
              i18n="@@action.upload"
            >
              Upload
            </button>
            <a
              [routerLink]="state.routeLink"
              id="cancel"
              i18n="@@action.cancel"
            >
              Cancel
            </a>
          </div>
        </div>
      </div>

      <div *ngIf="busy() === true">
        <kpn-monitor-route-form-save-step
          *ngFor="let step of steps()"
          [step]="step"
        />

        <kpn-monitor-route-form-errors [errors]="errors()" />

        <div class="kpn-button-group">
          <button
            mat-stroked-button
            id="goto-analysis-result-button"
            [routerLink]="state.routeLink"
            [disabled]="done() === false"
            i18n="@@monitor.route.gpx-delete.action.analysis-result"
          >
            Back to route details
          </button>
        </div>
      </div>

      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  styles: `
    .gpx-form {
      margin-top: 2rem;
      margin-left: 2rem;
      margin-bottom: 4rem;
    }
  `,
  providers: [MonitorRouteGpxService, MonitorWebsocketService, NavService],
  standalone: true,
  imports: [
    MatButtonModule,
    MonitorRouteGpxBreadcrumbComponent,
    MonitorRouteGpxReferenceComponent,
    NgIf,
    PageComponent,
    ReactiveFormsModule,
    RouterLink,
    SidebarComponent,
    MonitorRouteFormErrorsComponent,
    MonitorRouteFormSaveStepComponent,
    NgForOf,
  ],
})
export class MonitorRouteGpxComponent {
  readonly steps = this.monitorWebsocketService.steps;
  readonly errors = this.monitorWebsocketService.errors;
  readonly busy = this.monitorWebsocketService.busy;
  readonly done = this.monitorWebsocketService.done;

  readonly gpxReferenceDate = new FormControl<Date>(null, Validators.required);
  readonly referenceFilename = new FormControl<string>(
    null,
    Validators.required,
  );
  readonly referenceFile = new FormControl<File>(null);

  readonly form = new FormGroup({
    gpxReferenceDate: this.gpxReferenceDate,
    referenceFilename: this.referenceFilename,
    referenceFile: this.referenceFile,
  });

  constructor(
    protected service: MonitorRouteGpxService,
    private monitorWebsocketService: MonitorWebsocketService,
  ) {}

  save(): void {
    const referenceTimestamp = TimestampUtil.toTimestamp(
      this.gpxReferenceDate.value,
    );
    this.service.save(this.referenceFile.value, referenceTimestamp);
  }
}
