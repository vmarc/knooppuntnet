import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Reference } from '@api/common/common/reference';
import { NodeInfo } from '@api/common/node-info';
import { NodeNetworkReference } from '@api/common/node/node-network-reference';
import { NodeNetworkRouteReference } from '@api/common/node/node-network-route-reference';

@Component({
  selector: 'kpn-node-network-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <kpn-icon-network-link
        [reference]="toReference(reference)"
      ></kpn-icon-network-link>
      <div class="network-reference-details">
        <div class="text">
          <kpn-node-network-reference-statement
            [nodeInfo]="nodeInfo"
            [reference]="reference"
          >
          </kpn-node-network-reference-statement>
        </div>
        <div *ngFor="let routeReference of reference.routes" class="route-line">
          <kpn-icon-route-link
            [reference]="toRouteReference(routeReference)"
          ></kpn-icon-route-link>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .network-reference-details {
        margin-left: 35px;
        padding-bottom: 35px;
      }

      .text {
        padding-bottom: 10px;
      }

      ::ng-deep .text p {
        margin: 0 0 5px 0;
      }

      .route-line {
        height: 36px;
        display: flex;
        align-items: center;
      }
    `,
  ],
})
export class NodeNetworkReferenceComponent {
  @Input() nodeInfo: NodeInfo;
  @Input() reference: NodeNetworkReference;

  toReference(ref: NodeNetworkReference): Reference {
    return new Reference(
      ref.networkId,
      ref.networkName,
      ref.networkType,
      ref.nodeConnection
    );
  }

  toRouteReference(ref: NodeNetworkRouteReference): Reference {
    return new Reference(
      ref.routeId,
      ref.routeName,
      this.reference.networkType,
      ref.routeRole === 'connection'
    );
  }
}
