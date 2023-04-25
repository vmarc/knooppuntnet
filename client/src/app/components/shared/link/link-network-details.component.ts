import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkType } from '@api/custom';

@Component({
  selector: 'kpn-link-network-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="'/analysis/network/' + networkId"
      [state]="{networkType, networkName}"
    >
      {{ networkName }}
    </a>
  `,
})
export class LinkNetworkDetailsComponent {
  @Input() networkId: number;
  @Input() networkType: NetworkType;
  @Input() networkName: string;
}
