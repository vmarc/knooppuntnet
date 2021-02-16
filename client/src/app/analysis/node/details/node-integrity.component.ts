import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {NodeIntegrity} from '@api/common/node/node-integrity';
import {NodeIntegrityDetail} from '@api/common/node/node-integrity-detail';

@Component({
  selector: 'kpn-node-integrity',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="integrity.details.length === 0" i18n="@@node.integrity.none">
      There is no integrity information for this node
      (no expected_??n_route_relations tag).
    </p>

    <div *ngFor="let detail of integrity.details" class="detail">
      <div class="kpn-line detail-header">
        <kpn-network-type-icon [networkType]="detail.networkType"></kpn-network-type-icon>
        <div class="detail-header-text">
          <span *ngIf="happy(detail)" i18n="@@node.integrity.ok">
            The expected number of routes ({{detail.expectedRouteCount}}) matches the number of routes found.
          </span>
          <span *ngIf="!happy(detail)" i18n="@@node.integrity.not-ok">
            The actual number of routes in this node ({{detail.routeRefs.length}})
            does not match the expected number of routes ({{detail.expectedRouteCount}}).
          </span>
          <kpn-icon-happy *ngIf="happy(detail)"></kpn-icon-happy>
          <kpn-icon-investigate *ngIf="!happy(detail)"></kpn-icon-investigate>
        </div>
      </div>
      <div *ngFor="let ref of detail.routeRefs">
        <div class="kpn-line route-ref">
          <kpn-network-type-icon [networkType]="detail.networkType"></kpn-network-type-icon>
          <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
        </div>
      </div>
    </div>
  `,
  styles: [`

    .detail {
      padding-bottom: 1em;
    }

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

    .route-ref {
      padding-left: 2em;
      height: 36px;
    }

    kpn-icon-happy {
      position: relative;
      top: 3px;
    }

    kpn-icon-investigate {
      position: relative;
      top: 3px;
    }

  `]
})
export class NodeIntegrityComponent {

  @Input() integrity: NodeIntegrity;

  happy(detail: NodeIntegrityDetail): boolean {
    return detail.expectedRouteCount === detail.routeRefs.length;
  }

}
