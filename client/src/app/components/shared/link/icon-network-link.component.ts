import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Reference } from '@api/common/common';

@Component({
  selector: 'kpn-icon-network-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-icon-link
      [reference]="reference"
      [mixedNetworkScopes]="mixedNetworkScopes"
      [url]="'/analysis/network/' + reference.id"
    />
  `,
})
export class IconNetworkLinkComponent {
  @Input() reference: Reference;
  @Input() mixedNetworkScopes: boolean;
}
