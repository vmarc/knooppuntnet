import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { DataComponent } from '@app/components/shared/data';
import { TimestampDayPipe } from '@app/components/shared/format';
import { DistancePipe } from '@app/components/shared/format';
import { DayPipe } from '@app/components/shared/format';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { RouteSummaryComponent } from '@app/analysis/route';
import { Translations } from '@app/i18n';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorRouteFormErrorsComponent } from '../components/monitor-route-form-errors.component';
import { MonitorRouteFormSaveStepComponent } from '../components/monitor-route-form-save-step.component';
import { MonitorRouteGpxBreadcrumbComponent } from './monitor-route-gpx-breadcrumb.component';
import { MonitorRouteGpxService } from './monitor-route-gpx.service';

@Component({
  selector: 'kpn-monitor-route-gpx-delete',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (_state(); as state) {
      <kpn-page>
        <kpn-monitor-route-gpx-breadcrumb
          [groupName]="state.groupName"
          [groupLink]="state.groupLink"
          [routeName]="state.routeName"
          [routeLink]="state.routeLink"
        />
        @if (state.response; as response) {
          @if (!response.result) {
            <div i18n="@@monitor.route.gpx-delete.not-found" class="kpn-error">Route not found</div>
          }
          @if (response.result; as page) {
            <h1>{{ page.subRelationDescription }}</h1>
            <h2 i18n="@@monitor.route.gpx-delete.title">GPX reference</h2>

            <div class="gpx-form">
              <kpn-data title="File" i18n-title="@@monitor.route.gpx-delete.reference-filename">
                {{ page.referenceFilename }}
              </kpn-data>
              <kpn-data title="Reference day" i18n-title="@@monitor.route.gpx-delete.reference-day">
                {{ page.referenceTimestamp | yyyymmdd }}
              </kpn-data>
              <kpn-data title="Distance" i18n-title="@@monitor.route.gpx-delete.reference-distance">
                {{ page.referenceDistance | distance }}
              </kpn-data>
            </div>

            @if (busy() === false) {
              <div class="kpn-button-group">
                <button mat-stroked-button (click)="delete()">
                  <span class="delete-button" i18n="@@monitor.route.gpx.delete.action">
                    Delete reference
                  </span>
                </button>
                <a [routerLink]="state.routeLink" id="cancel">{{ cancelLinkText }}</a>
              </div>
            }
          }
        }

        @if (busy() === true) {
          @for (step of steps(); track $index) {
            <kpn-monitor-route-form-save-step [step]="step" />
          }
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
        }

        <kpn-sidebar sidebar />
      </kpn-page>
    }
  `,
  styles: `
    .gpx-form {
      margin-top: 2rem;
      margin-left: 2rem;
      margin-bottom: 4rem;
    }

    .delete-button {
      color: red;
    }
  `,
  providers: [MonitorRouteGpxService, MonitorWebsocketService, NavService],
  standalone: true,
  imports: [
    DataComponent,
    DayPipe,
    DistancePipe,
    MatButtonModule,
    MonitorRouteGpxBreadcrumbComponent,
    PageComponent,
    RouteSummaryComponent,
    RouterLink,
    SidebarComponent,
    TimestampDayPipe,
    MonitorRouteFormErrorsComponent,
    MonitorRouteFormSaveStepComponent,
  ],
})
export class MonitorRouteGpxDeleteComponent implements OnDestroy {
  private readonly service = inject(MonitorRouteGpxService);
  private readonly monitorWebsocketService = inject(MonitorWebsocketService);

  protected readonly cancelLinkText = Translations.get('@@action.cancel');
  protected _state = this.service.state;

  protected readonly steps = this.monitorWebsocketService.steps;
  protected readonly errors = this.monitorWebsocketService.errors;
  protected readonly busy = this.monitorWebsocketService.busy;
  protected readonly done = this.monitorWebsocketService.done;

  constructor() {
    this.monitorWebsocketService.reset();
  }

  ngOnDestroy(): void {
    this.monitorWebsocketService.reset();
  }

  delete(): void {
    this.service.delete();
  }
}
