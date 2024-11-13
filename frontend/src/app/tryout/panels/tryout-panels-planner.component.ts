import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { TryoutPanelsService } from './tryout-panels.service';

@Component({
  selector: 'kpn-tryout-panels-planner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button (click)="service.gotoMenu()">
      <mat-icon svgIcon="back" />
    </button>
    Plan a route
  `,
  standalone: true,
  imports: [MatIcon, MatIconButton],
})
export class TryoutPanelsPlannerComponent {
  readonly service = inject(TryoutPanelsService);
}
