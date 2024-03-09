import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  selector: 'kpn-icon-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<mat-icon svgIcon="node" matTooltip="node" i18n-matTooltip="@@icon.node.tooltip" />',
  standalone: true,
  imports: [MatIcon, MatTooltip],
})
export class IconNodeComponent {}
