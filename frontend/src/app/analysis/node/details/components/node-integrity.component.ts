import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { input } from '@angular/core';
import { NodeIntegrity } from '@api/common/node';
import { NodeIntegrityDetail } from '@api/common/node';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { RouteScopeNameComponent } from '@app/shared/components/route-scope-name.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';

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
              <nz-icon nzType="detail.routeType" />
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
                @if (mixedRouteScopes()) {
                  <span class="kpn-brackets kpn-thin">
                    <kpn-route-scope-name [routeScope]="detail.routeScope" />
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
  imports: [IconHappyComponent, IconInvestigateComponent, RouteScopeNameComponent, NzIconDirective],
})
export class NodeIntegrityComponent {
  integrity = input.required<NodeIntegrity>();
  mixedRouteScopes = input.required<boolean>();

  happy(detail: NodeIntegrityDetail): boolean {
    return detail.expectedRouteCount === detail.routeRefs.length;
  }
}
