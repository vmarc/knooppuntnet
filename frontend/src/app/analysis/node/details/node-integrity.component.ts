import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
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
    <p
      *ngIf="!integrity || integrity.details.length === 0"
      i18n="@@node.integrity.none"
    >
      There is no integrity information for this node (no
      expected_??n_route_relations tag).
    </p>

    <div *ngIf="integrity">
      <div *ngFor="let detail of integrity.details">
        <div class="kpn-line detail-header">
          <kpn-network-type-icon [networkType]="detail.networkType" />
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
            <kpn-icon-happy *ngIf="happy(detail)" />
            <kpn-icon-investigate *ngIf="!happy(detail)" />
            <span *ngIf="mixedNetworkScopes" class="kpn-brackets kpn-thin">
              <kpn-network-scope-name [networkScope]="detail.networkScope" />
            </span>
          </div>
        </div>
      </div>
    </div>
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
    NgFor,
    NgIf,
  ],
})
export class NodeIntegrityComponent {
  @Input() integrity: NodeIntegrity;
  @Input() mixedNetworkScopes: boolean;

  happy(detail: NodeIntegrityDetail): boolean {
    return detail.expectedRouteCount === detail.routeRefs.length;
  }
}
