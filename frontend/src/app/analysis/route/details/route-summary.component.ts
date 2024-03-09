import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteDetailsPageData } from '@api/common/route';
import { CountryNameComponent } from '@app/components/shared';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { JosmRelationComponent } from '@app/components/shared/link';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-route-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p>{{ (route().summary.meters | integer) + ' m' }}</p>

      @if (route().summary.country) {
        <p>
          <kpn-country-name [country]="route().summary.country" />
        </p>
      }

      <p>
        <kpn-osm-link-relation [relationId]="route().summary.id" />
        <span class="kpn-brackets-link">
          <kpn-josm-relation [relationId]="route().summary.id" />
        </span>
      </p>

      @if (isRouteBroken()) {
        <p class="kpn-line">
          <mat-icon svgIcon="warning" />
          <span i18n="@@route.broken">Something seems wrong with this route.</span>
        </p>
      }

      @if (isRouteIncomplete()) {
        <p class="kpn-line">
          <mat-icon svgIcon="warning" />
          <markdown i18n="@@route.incomplete">
            Route definition is incomplete (has tag *"fixme=incomplete"*).
          </markdown>
        </p>
      }

      @if (!route().active) {
        <p class="kpn-warning" i18n="@@route.not-active">This route is not active anymore.</p>
      }

      @if (isProposed()) {
        <p class="kpn-line">
          <mat-icon svgIcon="warning" style="min-width: 24px" />
          <markdown i18n="@@route.proposed">
            Proposed: this route has a tag _"state=proposed"_. The route is assumed to still be in a
            planning phase and likely not signposted in the field.
          </markdown>
        </p>
      }

      @if (isRouteNameDerivedFromNodes()) {
        <p class="kpn-line">
          <span i18n="@@route.name-derived-from-nodes">
            The route name is derived from the route nodes, rather than the tags in the route
            relation.
          </span>
        </p>
      }
    </div>
  `,
  standalone: true,
  imports: [
    CountryNameComponent,
    IntegerFormatPipe,
    JosmRelationComponent,
    MarkdownModule,
    MatIconModule,
    OsmLinkRelationComponent,
  ],
})
export class RouteSummaryComponent {
  route = input.required<RouteDetailsPageData>();

  isRouteBroken() {
    return this.route().facts.includes('RouteBroken');
  }

  isRouteIncomplete() {
    return this.route().facts.includes('RouteIncomplete');
  }

  isProposed() {
    const stateTag = this.route().tags.tags.find((t) => t.key === 'state');
    return stateTag && stateTag.value === 'proposed';
  }

  isRouteNameDerivedFromNodes(): boolean {
    return this.route().analysis?.nameDerivedFromNodes === true;
  }
}
