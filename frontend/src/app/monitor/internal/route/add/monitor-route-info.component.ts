import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';
import { DataComponent } from '@app/shared/components/data/data.component';
import { SymbolComponent } from '@app/symbol/symbol.component';

@Component({
  selector: 'ui-monitor-route-info',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    @if (!routeInfo().active) {
      <div class="kpn-line kpn-error">
        <mat-icon svgIcon="warning" />
        <span i18n="@@monitor.relation.not-found">
          Could not find an OSM relation with this id.
        </span>
      </div>
    }

    @if (routeInfo().active && !routeInfo().hasRouteTags) {
      <div class="kpn-line kpn-error">
        <mat-icon svgIcon="warning" />
        <span i18n="@@monitor.relation.no-route-tags">
          This OSM relation does not seem to have the required tags for a route.
        </span>
      </div>
    }

    @if (routeInfo().active && routeInfo().hasRouteTags) {
      <div>
        <div class="section-title" i18n="@@monitor.relation.title">Route information:</div>

        <div class="section-body">
          @if (routeInfo().ref) {
            <ui-data title="Ref" i18n-title="@@monitor.relation.ref">
              {{ routeInfo().ref }}
            </ui-data>
          }

          @if (routeInfo().name) {
            <ui-data title="Name" i18n-title="@@monitor.relation.name">
              {{ routeInfo().name }}
            </ui-data>
          }

          @if (routeInfo().from) {
            <ui-data title="From" i18n-title="@@monitor.relation.from">
              {{ routeInfo().from }}
            </ui-data>
          }

          @if (routeInfo().to) {
            <ui-data title="To" i18n-title="@@monitor.relation.to">
              {{ routeInfo().to }}
            </ui-data>
          }

          @if (routeInfo().operator) {
            <ui-data title="Operator" i18n-title="@@monitor.relation.operator">
              {{ routeInfo().operator }}
            </ui-data>
          }

          @if (routeInfo().website) {
            <ui-data title="Website" i18n-title="@@monitor.relation.website">
              <a
                class="external"
                rel="nofollow noreferrer"
                target="_blank"
                [href]="routeInfo().website"
              >
                {{ routeInfo().website }}
              </a>
            </ui-data>
          }

          @if (routeInfo().symbol) {
            <ui-data title="Symbol" i18n-title="@@monitor.relation.symbol">
              <p>{{ routeInfo().symbol }}</p>
              <ui-symbol [description]="routeInfo().symbol" />
            </ui-data>
          }
        </div>
      </div>
    }
  `,
  styles: `
    .section-title {
      padding-top: 2em;
    }

    .section-body {
      padding-top: 1em;
      padding-left: 2em;
    }
  `,
  imports: [DataComponent, SymbolComponent, MatIconModule],
})
export class MonitorRouteInfoComponent {
  readonly routeInfo = input.required<MonitorRouteInfoPage>();
}
