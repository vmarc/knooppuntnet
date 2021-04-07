import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';

@Component({
  selector: 'kpn-network-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="network-type">
      <kpn-network-type-icon
        [networkType]="networkType"
      ></kpn-network-type-icon>
      <kpn-network-type-name
        [networkType]="networkType"
      ></kpn-network-type-name>
      <ng-content></ng-content>
    </div>
  `,
  styles: [
    `
      .network-type {
        display: inline-flex;
        flex-direction: row;
        align-items: center;
      }

      kpn-network-type-icon {
        height: 24px;
        margin-right: 10px;
      }
    `,
  ],
})
export class NetworkTypeComponent {
  @Input() networkType: NetworkType;
}
