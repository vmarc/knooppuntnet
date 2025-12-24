import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { ActionButtonRelationComponent } from '@app/analysis/components/action/action-button-relation.component';

@Component({
  selector: 'ui-monitor-route-details-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!page().summary.relationId) {
      <p i18n="@@monitor.route.details.relation-id-undefined">
        Route relation has not been defined yet
      </p>
    } @else {
      <p class="kpn-space-separated">
        <span>{{ page().details.wayCount }}</span>
        <span i18n="@@monitor.route.details.ways">ways</span>
      </p>

      <p>{{ 'TODO page().details.osmDistance | distance' }}</p>

      @if (page().details.relationCount > 1) {
        <p class="kpn-small-spacer-above" i18n="@@monitor.route.details.relations">
          {{ page().details.relationCount }} relations in {{ page().details.relationLevels }} levels
        </p>
      }

      <div class="kpn-align-center">
        <span>{{ page().summary.relationId }}</span>
        <ui-action-button-relation [relationId]="page().summary.relationId" />
      </div>

      @if (page().symbol) {
        <div class="kpn-small-spacer-above">
          <ui-symbol [description]="page().symbol" />
        </div>
      }
    }
  `,
  imports: [ActionButtonRelationComponent, SymbolComponent],
})
export class MonitorRouteDetailsSummaryComponent {
  readonly page = input.required<MonitorRouteDetailsPage>();
}
