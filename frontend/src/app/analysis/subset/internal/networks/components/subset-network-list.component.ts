import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network/network-attributes';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { SubsetNetworkComponent } from './subset-network.component';

@Component({
  selector: 'ui-subset-network-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-items>
      @for (network of networks(); track network) {
        <ui-item [index]="$index">
          <ui-subset-network [network]="network" />
        </ui-item>
      }
    </ui-items>
  `,
  imports: [ItemsComponent, ItemComponent, SubsetNetworkComponent],
})
export class SubsetNetworkListComponent {
  readonly networks = input.required<ReadonlyArray<NetworkAttributes>>();
}
