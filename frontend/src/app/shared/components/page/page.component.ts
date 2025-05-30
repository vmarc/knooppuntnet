import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'ui-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <div class="page-contents">
      <ng-content />
    </div>
  `,
  styles: `
    .page-contents {
      margin: 1em;
    }
  `,
  imports: [],
})
export class PageComponent {}
