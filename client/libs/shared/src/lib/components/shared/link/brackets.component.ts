import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-brackets',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <span class="bracket">(</span>
    <ng-content />
    <span class="bracket">)</span>
  `,
  styles: [
    `
      .bracket {
        color: grey;
      }
    `,
  ],
  standalone: true,
})
export class BracketsComponent {}
