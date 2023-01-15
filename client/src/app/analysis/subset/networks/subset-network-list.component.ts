import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network/network-attributes';

@Component({
  selector: 'kpn-subset-network-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-items>
      <kpn-item *ngFor="let network of networks; let i = index" [index]="i">
        <kpn-subset-network [network]="network"/>
      </kpn-item>
    </kpn-items>
  `,
})
export class SubsetNetworkListComponent {
  @Input() networks: NetworkAttributes[];
}
