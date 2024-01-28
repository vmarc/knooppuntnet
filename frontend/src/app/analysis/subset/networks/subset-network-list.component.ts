import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
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
  standalone: true,
  imports: [ItemsComponent, ItemComponent, SubsetNetworkComponent],
})
export class SubsetNetworkListComponent {
  networks = input<NetworkAttributes[] | undefined>();
}
