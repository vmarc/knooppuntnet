import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { DataComponent } from '@app/components/shared/data';
import { DistancePipe } from '@app/components/shared/format';
import { DayPipe } from '@app/components/shared/format';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { RouteSummaryComponent } from '../../../../../analysis/src/lib/route/details/route-summary.component';
import { MonitorRouteGpxBreadcrumbComponent } from './monitor-route-gpx-breadcrumb.component';
import { MonitorRouteGpxService } from './monitor-route-gpx.service';

@Component({
  selector: 'kpn-monitor-route-gpx-delete',
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
        <div
          *ngIf="!response.result"
          i18n="@@monitor.route.gpx-delete.not-found"
        >
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
              [routerLink]="state.routeLink"
              id="cancel"
              i18n="@@action.cancel"
            >
              Cancel
            </a>
          </div>
        </div>
      </div>
      <kpn-sidebar sidebar />
    </kpn-page>
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
  providers: [MonitorRouteGpxService, NavService],
  standalone: true,
  imports: [
    DataComponent,
    DayPipe,
    DistancePipe,
    MatButtonModule,
    MonitorRouteGpxBreadcrumbComponent,
    NgIf,
    RouteSummaryComponent,
    RouterLink,
    PageComponent,
    SidebarComponent,
  ],
})
export class MonitorRouteGpxDeleteComponent {
  constructor(protected service: MonitorRouteGpxService) {}

  delete(): void {
    this.service.delete();
  }
}
