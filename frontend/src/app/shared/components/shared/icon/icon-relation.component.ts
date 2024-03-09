import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  selector: 'kpn-icon-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template:
    '<mat-icon svgIcon="relation" matTooltip="relation" i18n-matTooltip="@@icon.relation.tooltip" />',
  standalone: true,
  imports: [MatIcon, MatTooltip],
})
export class IconRelationComponent {}
