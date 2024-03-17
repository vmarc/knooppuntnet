import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { input } from '@angular/core';
import { NodeIntegrity } from '@api/common/node';
import { NodeIntegrityDetail } from '@api/common/node';
import { NetworkScopeNameComponent } from '@app/components/shared';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';

@Component({
  selector: 'kpn-node-integrity',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!integrity() || integrity().details.length === 0) {
      <p i18n="@@node.integrity.none">
        There is no integrity information for this node (no expected_??n_route_relations tag).
      </p>
    }

    @if (integrity()) {
      <div>
        @for (detail of integrity().details; track detail) {
          <div>
            <div class="kpn-line detail-header">
              <kpn-network-type-icon [networkType]="detail.networkType" />
              <div class="detail-header-text">
                @if (happy(detail)) {
                  <span i18n="@@node.integrity.ok">
                    The expected number of routes ({{ detail.expectedRouteCount }}) matches the
                    number of routes found.
                  </span>
                  <kpn-icon-happy />
                } @else {
                  <span i18n="@@node.integrity.not-ok">
                    The actual number of routes in this node ({{ detail.routeRefs.length }}) does
                    not match the expected number of routes ({{ detail.expectedRouteCount }}).
                  </span>
                  <kpn-icon-investigate />
                }
                @if (mixedNetworkScopes()) {
                  <span class="kpn-brackets kpn-thin">
                    <kpn-network-scope-name [networkScope]="detail.networkScope" />
                  </span>
                }
              </div>
            </div>
          </div>
        }
      </div>
    }
  `,
  styles: `
    .detail-header {
      padding-bottom: 0.5em;
    }

    .detail-header-text {
      display: inline;
      line-height: 24px;
    }

    .detail-header-text :not(:last-child) {
      padding-right: 0.8em;
    }

    kpn-icon-happy {
      position: relative;
      top: 3px;
    }

    kpn-icon-investigate {
      position: relative;
      top: 3px;
    }
  `,
  standalone: true,
  imports: [
    IconHappyComponent,
    IconInvestigateComponent,
    NetworkScopeNameComponent,
    NetworkTypeIconComponent,
  ],
})
export class NodeIntegrityComponent {
  integrity = input.required<NodeIntegrity>();
  mixedNetworkScopes = input.required<boolean>();

  happy(detail: NodeIntegrityDetail): boolean {
    return detail.expectedRouteCount === detail.routeRefs.length;
  }
}
