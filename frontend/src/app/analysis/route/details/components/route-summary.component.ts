import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteDetailsPageData } from '@api/common/route';
import { CountryNameComponent } from '@app/shared/components/country-name.component';
import { DividerComponent } from '@app/shared/components/divider.component';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { SymbolComponent } from '@app/symbol';
import { MarkdownModule } from 'ngx-markdown';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';
import { RouteLocationComponent } from './route-location.component';

@Component({
  selector: 'kpn-route-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p>{{ route().summary.meters | distance }}</p>

      @if (route().summary.countries) {
        <p>
          @for (country of route().summary.countries; track country) {
            <kpn-country-name [country]="country" />
          }
        </p>
      }

      <div class="kpn-align-center">
        <span>{{ route().summary.id }}</span>
        <kpn-action-button-route
          [routeType]="route().summary.routeTypes[0]"
          [relationId]="route().summary.id"
        />
      </div>

      <!-- TODO redesign routeTypes[0] -->
      <kpn-route-location
        [routeType]="route().summary.routeTypes[0]"
        [locationCandidateInfos]="route().locationCandidateInfos"
      />
      @if (hasSymbol()) {
        <kpn-symbol [description]="symbolDescription()" />
      }

      @if (hasAdditionalInformation()) {
        <kpn-divider />
      }

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
  imports: [
    ActionButtonRouteComponent,
    CountryNameComponent,
    DividerComponent,
    MarkdownModule,
    MatIconModule,
    RouteLocationComponent,
    SymbolComponent,
    DistancePipe,
    DistancePipe,
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
    const stateTag = this.route().summary.tags.find((t) => t.key === 'state');
    return stateTag && stateTag.value === 'proposed';
  }

  isRouteNameDerivedFromNodes(): boolean {
    return this.route()?.nameDerivedFromNodes === true;
  }

  hasAdditionalInformation(): boolean {
    return (
      this.isRouteBroken() ||
      this.isRouteIncomplete() ||
      !this.route().active ||
      this.isProposed() ||
      this.isRouteNameDerivedFromNodes()
    );
  }

  hasSymbol(): boolean {
    return !!this.symbolDescription();
  }

  symbolDescription(): string {
    const symbolTag = this.route().summary.tags.find((tag) => tag.key === 'osmc:symbol');
    if (symbolTag) {
      return symbolTag.value;
    }
    return undefined;
  }
}
