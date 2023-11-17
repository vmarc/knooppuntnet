import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MonitorRouteInfoPage } from '@api/common/monitor';
import { DataComponent } from '@app/components/shared/data';
import { SymbolComponent } from '@app/symbol';

@Component({
  selector: 'kpn-monitor-route-info',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!routeInfo.active" class="kpn-line kpn-error">
      <mat-icon svgIcon="warning" />
      <span i18n="@@monitor.relation.not-found">
        Could not find an OSM relation with this id.
      </span>
    </div>

    <div
      *ngIf="routeInfo.active && !routeInfo.hasRouteTags"
      class="kpn-line kpn-error"
    >
      <mat-icon svgIcon="warning" />
      <span i18n="@@monitor.relation.no-route-tags">
        This OSM relation does not seem to have the required tags for a route.
      </span>
    </div>

    <div *ngIf="routeInfo.active && routeInfo.hasRouteTags">
      <div class="section-title" i18n="@@monitor.relation.title">
        Route information:
      </div>

      <div class="section-body">
        <kpn-data
          *ngIf="routeInfo.ref"
          title="Ref"
          i18n-title="@@monitor.relation.ref"
        >
          {{ routeInfo.ref }}
        </kpn-data>

        <kpn-data
          *ngIf="routeInfo.name"
          title="Name"
          i18n-title="@@monitor.relation.name"
        >
          {{ routeInfo.name }}
        </kpn-data>

        <kpn-data
          *ngIf="routeInfo.from"
          title="From"
          i18n-title="@@monitor.relation.from"
        >
          {{ routeInfo.from }}
        </kpn-data>

        <kpn-data
          *ngIf="routeInfo.to"
          title="To"
          i18n-title="@@monitor.relation.to"
        >
          {{ routeInfo.to }}
        </kpn-data>

        <kpn-data
          *ngIf="routeInfo.operator"
          title="Operator"
          i18n-title="@@monitor.relation.operator"
        >
          {{ routeInfo.operator }}
        </kpn-data>

        <kpn-data
          *ngIf="routeInfo.website"
          title="Website"
          i18n-title="@@monitor.relation.website"
        >
          <a
            class="external"
            rel="nofollow noreferrer"
            target="_blank"
            [href]="routeInfo.website"
          >
            {{ routeInfo.website }}
          </a>
        </kpn-data>

        <kpn-data
          *ngIf="routeInfo.symbol"
          title="Symbol"
          i18n-title="@@monitor.relation.symbol"
        >
          <p>{{ routeInfo.symbol }}</p>
          <kpn-symbol [description]="routeInfo.symbol" />
        </kpn-data>
      </div>
    </div>
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
  imports: [NgIf, DataComponent, SymbolComponent, MatIconModule],
})
export class MonitorRouteInfoComponent {
  @Input({ required: true }) routeInfo: MonitorRouteInfoPage;
}
