import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'ui-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="menu-wrapper">
      <div class="menu">
        <ng-content />
      </div>
      <div class="menu-extra">
        <ng-content select="[menu-extra-item]" />
      </div>
    </div>
  `,
  styles: `
    .menu-wrapper {
      display: flex;
      border-bottom: 1px solid lightgray;
    }

    .menu {
      line-height: 30px;
    }

    .menu-extra {
      line-height: 30px;
      margin-left: auto;
    }

    ::ng-deep .menu :not(:last-child):after {
      content: ' | ';
      padding-left: 5px;
      padding-right: 5px;
    }
  `,
  imports: [MatDividerModule],
})
export class PageMenuComponent {}
