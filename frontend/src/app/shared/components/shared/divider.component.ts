import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDivider } from '@angular/material/divider';

@Component({
  selector: 'kpn-divider',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-small-spacer-above kpn-small-spacer-below">
      <mat-divider />
    </div>
  `,
  standalone: true,
  imports: [MatDivider],
})
export class DividerComponent {}
