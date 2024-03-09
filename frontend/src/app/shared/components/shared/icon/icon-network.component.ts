import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  selector: 'kpn-icon-network',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template:
    '<mat-icon svgIcon="network" matTooltip="node network" i18n-matTooltip="@@icon.network.tooltip" />',
  standalone: true,
  imports: [MatIcon, MatTooltip],
})
export class IconNetworkComponent {}
