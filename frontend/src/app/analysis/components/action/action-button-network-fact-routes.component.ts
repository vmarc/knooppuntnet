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
import { NetworkFact } from '@api/common';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      mat-icon-button
      matTooltip="Open network fact action menu"
      [matMenuTriggerFor]="menu"
      (click)="$event.stopPropagation()"
    >
      <mat-icon svgIcon="open-in-new" class="action-button-icon" />
    </button>
    <mat-menu #menu="matMenu" class="menu-fit-width">
      <button mat-menu-item (click)="josmLoadRelations()">JOSM load route relations</button>
      <button mat-menu-item (click)="josmLoadRelationsAndMembers()">
        JOSM load relations and members
      </button>
    </mat-menu>
  `,
  standalone: true,
  imports: [MatIcon, MatIconButton, MatMenu, MatMenuItem, MatMenuTrigger, MatTooltipModule],
})
export class ActionButtonNetworkFactRoutesComponent {
  networkFact = input.required<NetworkFact>();

  private readonly actionService = inject(ActionService);

  josmLoadRelations(): void {
    this.actionService.josmLoadRelations(this.relationIds());
  }

  josmLoadRelationsAndMembers(): void {
    this.actionService.josmLoadRelationsAndMembers(this.relationIds());
  }

  private relationIds(): Array<number> {
    return this.networkFact().elementIds;
  }
}
