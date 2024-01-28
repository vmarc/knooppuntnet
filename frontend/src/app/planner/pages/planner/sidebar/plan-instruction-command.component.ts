import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'kpn-plan-instruction-command',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <mat-icon svgIcon="{{ command() }}" class="command-icon" /> `,
  styles: `
    .command-icon {
      width: 40px;
      height: 40px;
    }

    ::ng-deep .command-icon > svg {
      width: 40px;
      height: 40px;
      fill: #666666;
      stroke: #666666;
    }
  `,
  standalone: true,
  imports: [MatIconModule],
})
export class PlanInstructionCommandComponent {
  command = input<string | undefined>();
}
