import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';

@Component({
  selector: 'kpn-network-type-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <mat-icon [svgIcon]="networkType"></mat-icon> `,
})
export class NetworkTypeIconComponent {
  @Input() networkType: NetworkType;
}
