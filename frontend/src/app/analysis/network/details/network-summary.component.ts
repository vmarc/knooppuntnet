import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NetworkDetailsPage } from '@api/common/network';
import { CountryNameComponent } from '@app/components/shared';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { JosmRelationComponent } from '@app/components/shared/link';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-network-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (!page().active) {
      <p class="kpn-warning" i18n="@@network-details.not-active">
        This network is not active anymore.
      </p>
    } @else {
      <p class="kpn-comma-list">
        <span>
          <!-- nested spans needed to combine the ::after's in kpn-comma-list and kpn-km -->
          <span class="kpn-km">{{ page().attributes.km | integer }}</span>
        </span>
        <span>
          {{ page().summary.nodeCount | integer }}
          <ng-container i18n="@@network-details.nodes">nodes</ng-container>
        </span>
        <span>
          {{ page().summary.routeCount | integer }}
          <ng-container i18n="@@network-details.routes">routes</ng-container>
        </span>
      </p>
    }

    <p>
      <kpn-country-name [country]="page().attributes.country" />
    </p>

    <p class="kpn-line">
      <kpn-osm-link-relation [relationId]="page().attributes.id" />
      <kpn-josm-relation [relationId]="page().attributes.id" />
    </p>

    @if (page().active && page().attributes.brokenRouteCount > 0) {
      <p class="kpn-line">
        <mat-icon svgIcon="warning" />
        <span i18n="@@network-details.contains-broken-routes">
          This network contains broken (non-continuous) routes.
        </span>
      </p>
    }

    @if (page().active && isProposed()) {
      <p class="kpn-line">
        <mat-icon svgIcon="warning" style="min-width: 24px" />
        <markdown i18n="@@network.proposed">
          Proposed: this network has tag _"state=proposed"_. The network is assumed to still be in a
          planning phase and likely not signposted in the field.
        </markdown>
      </p>
    }
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
export class NetworkSummaryComponent {
  page = input<NetworkDetailsPage | undefined>();

  isProposed() {
    const stateTag = this.page().tags.tags.find((t) => t.key === 'state');
    return stateTag && stateTag.value === 'proposed';
  }
}
