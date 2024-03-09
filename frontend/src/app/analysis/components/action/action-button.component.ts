import { ViewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenuItem } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'kpn-action-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      mat-icon-button
      matTooltip="Open action menu"
      [matMenuTriggerFor]="menu"
      (click)="$event.stopPropagation()"
    >
      <mat-icon svgIcon="open-in-new" class="action-button-icon" />
    </button>
    <mat-menu #menu="matMenu">
      <button mat-menu-item>Open in openstreetmap.org</button>
      <button mat-menu-item>JOSM load</button>
      <button mat-menu-item>JOSM go here</button>
    </mat-menu>
  `,
  standalone: true,
  imports: [MatIconButton, MatIcon, MatTooltipModule, MatMenu, MatMenuItem, MatMenuTrigger],
})
export class ActionButtonComponent {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
}
