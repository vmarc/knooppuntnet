import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NodeInfo } from '@api/common';
import { Reference } from '@api/common/common';
import { IconNetworkLinkComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-node-network-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <kpn-icon-network-link [reference]="reference" [mixedNetworkScopes]="mixedNetworkScopes" />
    </div>
  `,
  standalone: true,
  imports: [IconNetworkLinkComponent],
})
export class NodeNetworkReferenceComponent {
  @Input() nodeInfo: NodeInfo;
  @Input() reference: Reference;
  @Input() mixedNetworkScopes: boolean;
}
