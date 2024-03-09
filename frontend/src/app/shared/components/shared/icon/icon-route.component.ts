import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  selector: 'kpn-icon-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template:
    '<mat-icon svgIcon="route" matTooltip="route" i18n-matTooltip="@@icon.route.tooltip" />',
  standalone: true,
  imports: [MatIcon, MatTooltip],
})
export class IconRouteComponent {}
