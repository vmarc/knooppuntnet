import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { SubsetNetworkComponent } from './subset-network.component';

@Component({
  selector: 'kpn-subset-network-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-items>
      <kpn-item *ngFor="let network of networks; let i = index" [index]="i">
        <kpn-subset-network [network]="network" />
      </kpn-item>
    </kpn-items>
  `,
  standalone: true,
  imports: [ItemsComponent, NgFor, ItemComponent, SubsetNetworkComponent],
})
export class SubsetNetworkListComponent {
  @Input() networks: NetworkAttributes[];
}
