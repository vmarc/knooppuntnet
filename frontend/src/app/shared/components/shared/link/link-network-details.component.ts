import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NetworkType } from '@api/custom';

@Component({
  selector: 'kpn-link-network-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="'/analysis/network/' + networkId()"
      [state]="{ networkType: networkType(), networkName: networkName() }"
    >
      {{ networkName() }}
    </a>
  `,
  standalone: true,
  imports: [RouterLink],
})
export class LinkNetworkDetailsComponent {
  networkId = input.required<number>();
  networkName = input.required<string>();
  networkType = input<NetworkType>();
}
