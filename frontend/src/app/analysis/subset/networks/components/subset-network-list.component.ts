import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { SubsetNetworkComponent } from './subset-network.component';

@Component({
  selector: 'kpn-subset-network-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-items>
      @for (network of networks(); track network; let i = $index) {
        <kpn-item [index]="i">
          <kpn-subset-network [network]="network" />
        </kpn-item>
      }
    </kpn-items>
  `,
  imports: [ItemsComponent, ItemComponent, SubsetNetworkComponent],
})
export class SubsetNetworkListComponent {
  networks = input.required<NetworkAttributes[]>();
}
