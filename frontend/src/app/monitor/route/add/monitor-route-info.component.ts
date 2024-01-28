import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MonitorRouteInfoPage } from '@api/common/monitor';
import { DataComponent } from '@app/components/shared/data';
import { SymbolComponent } from '@app/symbol';

@Component({
  selector: 'kpn-monitor-route-info',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
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
            <kpn-data title="Ref" i18n-title="@@monitor.relation.ref">
              {{ routeInfo().ref }}
            </kpn-data>
          }

          @if (routeInfo().name) {
            <kpn-data title="Name" i18n-title="@@monitor.relation.name">
              {{ routeInfo().name }}
            </kpn-data>
          }

          @if (routeInfo().from) {
            <kpn-data title="From" i18n-title="@@monitor.relation.from">
              {{ routeInfo().from }}
            </kpn-data>
          }

          @if (routeInfo().to) {
            <kpn-data title="To" i18n-title="@@monitor.relation.to">
              {{ routeInfo().to }}
            </kpn-data>
          }

          @if (routeInfo().operator) {
            <kpn-data title="Operator" i18n-title="@@monitor.relation.operator">
              {{ routeInfo().operator }}
            </kpn-data>
          }

          @if (routeInfo().website) {
            <kpn-data title="Website" i18n-title="@@monitor.relation.website">
              <a
                class="external"
                rel="nofollow noreferrer"
                target="_blank"
                [href]="routeInfo().website"
              >
                {{ routeInfo().website }}
              </a>
            </kpn-data>
          }

          @if (routeInfo().symbol) {
            <kpn-data title="Symbol" i18n-title="@@monitor.relation.symbol">
              <p>{{ routeInfo().symbol }}</p>
              <kpn-symbol [description]="routeInfo().symbol" />
            </kpn-data>
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
  standalone: true,
  imports: [DataComponent, SymbolComponent, MatIconModule],
})
export class MonitorRouteInfoComponent {
  routeInfo = input.required<MonitorRouteInfoPage>();
}
