import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenuItem } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-nodes',
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
    <mat-menu #menu="matMenu" class="menu-fit-width">
      <button mat-menu-item (click)="josmLoad()">JOSM load nodes</button>
    </mat-menu>
  `,
  standalone: true,
  imports: [MatIcon, MatIconButton, MatMenu, MatMenuItem, MatMenuTrigger, MatTooltipModule],
})
export class ActionButtonNodesComponent {
  nodeIds = input.required<number[]>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    this.actionService.josmLoadNodes(this.nodeIds());
  }
}
