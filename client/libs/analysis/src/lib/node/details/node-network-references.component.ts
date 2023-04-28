import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NodeInfo } from '@api/common';
import { Reference } from '@api/common/common';
import { NodeNetworkReferenceComponent } from './node-network-reference.component';

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
        [mixedNetworkScopes]="mixedNetworkScopes"
      />
    </p>
  `,
  standalone: true,
  imports: [NgIf, NgFor, NodeNetworkReferenceComponent],
})
export class NodeNetworkReferencesComponent {
  @Input() nodeInfo: NodeInfo;
  @Input() references: Reference[];
  @Input() mixedNetworkScopes: boolean;
}
