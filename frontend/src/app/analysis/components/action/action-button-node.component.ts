import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatDivider } from '@angular/material/divider';
import { MatIcon } from '@angular/material/icon';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenuItem } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      mat-icon-button
      matTooltip="Open node action menu"
      [matMenuTriggerFor]="menu"
      (click)="$event.stopPropagation()"
    >
      <mat-icon svgIcon="open-in-new" class="action-button-icon" />
    </button>
    <mat-menu #menu="matMenu" class="menu-fit-width">
      <button mat-menu-item (click)="josmLoad()">JOSM load node</button>
      <button mat-menu-item (click)="josmZoom()">JOSM zoom/pan to node</button>
      <mat-divider />
      <button mat-menu-item (click)="id()">Open in iD</button>
      <button mat-menu-item (click)="osm()">Open in openstreetmap.org</button>
      <button mat-menu-item (click)="deepHistory()">Open in OSM Deep History</button>
    </mat-menu>
  `,
  standalone: true,
  imports: [
    MatIconButton,
    MatIcon,
    MatTooltipModule,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger,
    MatDivider,
  ],
})
export class ActionButtonNodeComponent {
  nodeId = input.required<number>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    this.actionService.josmLoadNode(this.nodeId());
  }

  josmZoom(): void {
    this.actionService.josmZoomNode(this.nodeId());
  }

  id(): void {
    this.actionService.idNode(this.nodeId());
  }

  osm(): void {
    this.actionService.osmNode(this.nodeId());
  }

  deepHistory(): void {
    this.actionService.deepHistoryNode(this.nodeId());
  }
}
