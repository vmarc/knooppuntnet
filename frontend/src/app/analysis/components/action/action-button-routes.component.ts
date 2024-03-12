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
  selector: 'kpn-action-button-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      mat-icon-button
      matTooltip="Open route action menu"
      [matMenuTriggerFor]="menu"
      (click)="$event.stopPropagation()"
    >
      <mat-icon svgIcon="open-in-new" class="action-button-icon" />
    </button>
    <mat-menu #menu="matMenu" class="menu-fit-width">
      <button mat-menu-item (click)="josmLoad()">JOSM load route relation</button>
      <button mat-menu-item (click)="josmLoadFull()">JOSM load relation and members</button>
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
export class ActionButtonRoutesComponent {
  relationIds = input.required<number[]>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    this.actionService.josmLoadRelations(this.relationIds());
  }

  josmLoadFull(): void {
    this.actionService.josmLoadRelationsAndMembers(this.relationIds());
  }
}
