import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';

@Component({
  selector: 'kpn-page',
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
  imports: [MatSidenavModule],
})
export class PageComponent {}
