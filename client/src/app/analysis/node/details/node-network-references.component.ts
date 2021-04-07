import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NodeInfo } from '@api/common/node-info';
import { NodeNetworkReference } from '@api/common/node/node-network-reference';

@Component({
  selector: 'kpn-node-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="references.length === 0" i18n="@@node.network-references.none">
      None
    </p>
    <p *ngFor="let reference of references">
      <kpn-node-network-reference
        [nodeInfo]="nodeInfo"
        [reference]="reference"
      ></kpn-node-network-reference>
    </p>
  `,
})
export class NodeNetworkReferencesComponent {
  @Input() nodeInfo: NodeInfo;
  @Input() references: NodeNetworkReference[];
}
