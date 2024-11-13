import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { TryoutPanelsService } from './tryout-panels.service';

@Component({
  selector: 'kpn-tryout-panels-configuration',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button (click)="service.gotoMenu()">
      <mat-icon svgIcon="back" />
    </button>
    Configuration
  `,
  standalone: true,
  imports: [MatIcon, MatIconButton],
})
export class TryoutPanelsConfigurationComponent {
  readonly service = inject(TryoutPanelsService);
}
