import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  selector: 'kpn-icon-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<mat-icon svgIcon="way" matTooltip="way" i18n-matTooltip="@@icon.way.tooltip" />',
  standalone: true,
  imports: [MatIcon, MatTooltip],
})
export class IconWayComponent {}
