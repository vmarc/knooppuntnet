import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouterLink } from '@angular/router';
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
  standalone: true,
  imports: [RouterLink],
})
export class LinkNetworkDetailsComponent {
  @Input({ required: true }) networkId: number;
  @Input({ required: true }) networkName: string;
  @Input({ required: false }) networkType: NetworkType;
}
