import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeInfo } from '@api/common';
import { Reference } from '@api/common/common';
import { NodeNetworkReferenceComponent } from './node-network-reference.component';

@Component({
  selector: 'kpn-node-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (references().length === 0) {
      <p i18n="@@node.network-references.none">None</p>
    }
    @for (reference of references(); track reference) {
      <p>
        <kpn-node-network-reference
          [nodeInfo]="nodeInfo()"
          [reference]="reference"
          [mixedNetworkScopes]="mixedNetworkScopes()"
        />
      </p>
    }
  `,
  standalone: true,
  imports: [NodeNetworkReferenceComponent],
})
export class NodeNetworkReferencesComponent {
  nodeInfo = input<NodeInfo | undefined>();
  references = input<Reference[] | undefined>();
  mixedNetworkScopes = input<boolean | undefined>();
}
