import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Translations } from '@app/shared/i18n/translations';
import { DataComponent } from '@app/shared/components/data/data.component';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { TimestampDayPipe } from '@app/shared/components/format/timestamp-day.pipe';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorRouteFormErrorsComponent } from '../components/monitor-route-form-errors.component';
import { MonitorRouteFormSaveStepComponent } from '../components/monitor-route-form-save-step.component';
import { MonitorRouteGpxBreadcrumbComponent } from './monitor-route-gpx-breadcrumb.component';
import { MonitorRouteGpxService } from './monitor-route-gpx.service';

@Component({
  selector: 'ui-monitor-route-gpx-delete',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (_state(); as state) {
      <ui-page>
        <ui-monitor-route-gpx-breadcrumb
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
              <ui-data title="File" i18n-title="@@monitor.route.gpx-delete.reference-filename">
                {{ page.referenceFilename }}
              </ui-data>
              <ui-data title="Reference day" i18n-title="@@monitor.route.gpx-delete.reference-day">
                {{ page.referenceTimestamp | yyyymmdd }}
              </ui-data>
              <ui-data title="Distance" i18n-title="@@monitor.route.gpx-delete.reference-distance">
                {{ page.referenceDistance | distance }}
              </ui-data>
            </div>

            @if (busy() === false) {
              <div class="kpn-button-group">
                <button nz-button (click)="delete()">
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
            <ui-monitor-route-form-save-step [step]="step" />
          }
          <ui-monitor-route-form-errors [errors]="errors()" />

          <div class="kpn-button-group">
            <button
              nz-button
              id="goto-analysis-result-button"
              [routerLink]="state.routeLink"
              [disabled]="done() === false"
              i18n="@@monitor.route.gpx-delete.action.analysis-result"
            >
              Back to route details
            </button>
          </div>
        }
      </ui-page>
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
  imports: [
    DataComponent,
    DistancePipe,
    MonitorRouteFormErrorsComponent,
    MonitorRouteFormSaveStepComponent,
    MonitorRouteGpxBreadcrumbComponent,
    PageComponent,
    RouterLink,
    TimestampDayPipe,
    TimestampDayPipe,
    DistancePipe,
    NzButtonComponent,
  ],
})
export class MonitorRouteGpxDeleteComponent implements OnDestroy {
  private readonly service = inject(MonitorRouteGpxService);
  private readonly monitorWebsocketService = inject(MonitorWebsocketService);

  readonly cancelLinkText = Translations.get('action.cancel');
  protected _state = this.service.state;

  readonly steps = this.monitorWebsocketService.steps;
  readonly errors = this.monitorWebsocketService.errors;
  readonly busy = this.monitorWebsocketService.busy;
  readonly done = this.monitorWebsocketService.done;

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
