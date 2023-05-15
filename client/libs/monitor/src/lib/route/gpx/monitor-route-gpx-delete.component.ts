import { NgIf } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { DataComponent } from '@app/components/shared/data';
import { DistancePipe } from '@app/components/shared/format';
import { DayPipe } from '@app/components/shared/format';
import { RouteSummaryComponent } from '../../../../../analysis/src/lib/route/details/route-summary.component';
import { MonitorRouteGpxBreadcrumbComponent } from './monitor-route-gpx-breadcrumb.component';
import { MonitorRouteGpxService } from './monitor-route-gpx.service';

@Component({
  selector: 'kpn-monitor-route-gpx-delete',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-gpx-breadcrumb
      [groupName]="service.groupName()"
      [groupLink]="service.groupLink()"
      [routeName]="service.routeName()"
      [routeLink]="service.routeLink()"
    />

    <div *ngIf="service.apiResponse() as response">
      <div *ngIf="!response.result" i18n="@@monitor.route.gpx-delete.not-found">
        Route not found
      </div>

      <div *ngIf="response.result as page">
        <h1>{{ page.subRelationDescription }}</h1>
        <h2 i18n="@@monitor.route.gpx-delete.title">GPX reference</h2>

        <div class="gpx-form">
          <kpn-data
            title="File"
            i18n-title="@@monitor.route.gpx-delete.reference-filename"
          >
            {{ page.referenceFilename }}
          </kpn-data>
          <kpn-data
            title="Reference day"
            i18n-title="@@monitor.route.gpx-delete.reference-day"
          >
            {{ page.referenceDay | day }}
          </kpn-data>
          <kpn-data
            title="Distance"
            i18n-title="@@monitor.route.gpx-delete.reference-distance"
          >
            {{ page.referenceDistance | distance }}
          </kpn-data>
        </div>

        <div class="kpn-button-group">
          <button
            mat-stroked-button
            class="delete-button"
            (click)="delete()"
            i18n="@@monitor.route.gpx.delete.action"
          >
            Delete reference
          </button>
          <a
            [routerLink]="service.routeLink()"
            id="cancel"
            i18n="@@action.cancel"
          >
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

      .delete-button {
        color: red;
      }
    `,
  ],
  standalone: true,
  providers: [MonitorRouteGpxService],
  imports: [
    NgIf,
    RouterLink,
    MatButtonModule,
    MonitorRouteGpxBreadcrumbComponent,
    DayPipe,
    DataComponent,
    RouteSummaryComponent,
    DistancePipe,
  ],
})
export class MonitorRouteGpxDeleteComponent implements OnInit {
  constructor(protected service: MonitorRouteGpxService) {}

  ngOnInit(): void {
    this.service.init();
  }

  delete(): void {
    this.service.delete();
  }
}
