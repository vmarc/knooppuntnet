import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { NodeIntegrity } from '@api/common/node/node-integrity';
import { NodeIntegrityDetail } from '@api/common/node/node-integrity-detail';

@Component({
  selector: 'kpn-node-integrity',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="integrity.details.length === 0" i18n="@@node.integrity.none">
      There is no integrity information for this node (no
      expected_??n_route_relations tag).
    </p>

    <div *ngIf="integrity">
      <div *ngFor="let detail of integrity.details">
        <div class="kpn-line detail-header">
          <kpn-network-type-icon
            [networkType]="detail.networkType"
          ></kpn-network-type-icon>
          <div class="detail-header-text">
            <span *ngIf="happy(detail)" i18n="@@node.integrity.ok">
              The expected number of routes ({{ detail.expectedRouteCount }})
              matches the number of routes found.
            </span>
            <span *ngIf="!happy(detail)" i18n="@@node.integrity.not-ok">
              The actual number of routes in this node ({{
                detail.routeRefs.length
              }}) does not match the expected number of routes ({{
                detail.expectedRouteCount
              }}).
            </span>
            <kpn-icon-happy *ngIf="happy(detail)"></kpn-icon-happy>
            <kpn-icon-investigate *ngIf="!happy(detail)"></kpn-icon-investigate>
            <span *ngIf="mixedNetworkScopes" class="kpn-brackets kpn-thin">
              <kpn-network-scope-name
                [networkScope]="detail.networkScope"
              ></kpn-network-scope-name>
            </span>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
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
  ],
})
export class NodeIntegrityComponent {
  @Input() integrity: NodeIntegrity;
  @Input() mixedNetworkScopes: boolean;

  happy(detail: NodeIntegrityDetail): boolean {
    return detail.expectedRouteCount === detail.routeRefs.length;
  }
}
