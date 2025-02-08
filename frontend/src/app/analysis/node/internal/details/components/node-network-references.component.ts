import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeInfo } from '@api/common/node-info';
import { Reference } from '@api/common/common/reference';
import { NodeNetworkReferenceComponent } from './node-network-reference.component';

@Component({
  selector: 'kpn-node-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (reference of references(); track reference.id) {
      <p>
        <kpn-node-network-reference
          [nodeInfo]="nodeInfo()"
          [reference]="reference"
          [mixedRouteScopes]="mixedRouteScopes()"
        />
      </p>
    } @empty {
      <p i18n="@@node.network-references.none">None</p>
    }
  `,
  imports: [NodeNetworkReferenceComponent],
})
export class NodeNetworkReferencesComponent {
  nodeInfo = input.required<NodeInfo>();
  references = input.required<Reference[]>();
  mixedRouteScopes = input.required<boolean>();
}
