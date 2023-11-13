import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkType } from '@api/custom';
import { NetworkTypeIconComponent } from './network-type-icon.component';
import { NetworkTypeNameComponent } from './network-type-name.component';

@Component({
  selector: 'kpn-network-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="network-type">
      <kpn-network-type-icon [networkType]="networkType" />
      <kpn-network-type-name [networkType]="networkType" />
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
  standalone: true,
  imports: [NetworkTypeIconComponent, NetworkTypeNameComponent],
})
export class NetworkTypeComponent {
  @Input({ required: true }) networkType: NetworkType;
}
