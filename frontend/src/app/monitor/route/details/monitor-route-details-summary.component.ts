import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { DistancePipe } from '@app/components/shared/format';
import { JosmRelationComponent } from '@app/components/shared/link';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { SymbolComponent } from '@app/symbol';

@Component({
  selector: 'kpn-monitor-route-details-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!page.relationId) {
      <p i18n="@@monitor.route.details.relation-id-undefined">
        Route relation has not been defined yet
      </p>
    } @else {
      <p class="kpn-separated">
        <kpn-osm-link-relation
          [title]="page.relationId.toString()"
          [relationId]="page.relationId"
        />
        <kpn-josm-relation [relationId]="page.relationId" />
      </p>

      <p class="kpn-space-separated">
        <span>{{ page.wayCount }}</span>
        <span i18n="@@monitor.route.details.ways">ways</span>
      </p>

      <p>{{ page.osmDistance | distance }}</p>

      @if (page.relationCount > 1) {
        <p class="kpn-small-spacer-above" i18n="@@monitor.route.details.relations">
          {{ page.relationCount }} relations in {{ page.relationLevels }} levels
        </p>
      }

      @if (page.symbol) {
        <div class="kpn-small-spacer-above">
          <kpn-symbol [description]="page.symbol" />
        </div>
      }
    }
  `,
  standalone: true,
  imports: [DistancePipe, JosmRelationComponent, OsmLinkRelationComponent, SymbolComponent],
})
export class MonitorRouteDetailsSummaryComponent {
  @Input({ required: true }) page: MonitorRouteDetailsPage;
}
