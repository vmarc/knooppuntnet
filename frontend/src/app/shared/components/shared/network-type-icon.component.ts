import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NetworkType } from '@api/custom';

@Component({
  selector: 'kpn-network-type-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <mat-icon [svgIcon]="networkType()" /> `,
  standalone: true,
  imports: [MatIconModule],
})
export class NetworkTypeIconComponent {
  networkType = input.required<NetworkType>();
}
