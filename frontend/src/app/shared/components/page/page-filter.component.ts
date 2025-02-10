import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from './page.component';

@Component({
  selector: 'kpn-page-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <div class="page">
        <div class="page-left">
          <div>
            <ng-content select="[filter]" />
          </div>
        </div>
        <div class="page-right">
          <ng-content />
        </div>
      </div>
    </kpn-page>
  `,
  styles: `
    .page {
      display: flex;
      height: 100%;
    }

    .page-left {
      position: absolute;
      top: 0;
      width: 20em;
      height: 100%;
      border-right: 1px solid lightgray;
      margin-right: 1em;
      overflow-y: auto;
    }

    .page-right {
      margin-left: 22em;
      flex-grow: 1;
    }
  `,
  imports: [PageComponent],
})
export class PageFilterComponent {}
