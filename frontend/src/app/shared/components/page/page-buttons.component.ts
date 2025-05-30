import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'ui-page-buttons',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <div class="page-buttons">
      <ng-content />
    </div>
  `,
  styles: `
    .page-buttons {
      display: flex;
      align-items: center;
      gap: 0.5em;
    }
  `,
})
export class PageButtonsComponent {}
