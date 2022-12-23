import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="menu-wrapper">
      <div class="menu">
        <ng-content></ng-content>
      </div>
      <div class="menu-extra">
        <ng-content select=".menu-extra-item" class=""></ng-content>
      </div>
    </div>
    <mat-divider></mat-divider>
  `,
  styles: [
    `
      .menu-wrapper {
        display: flex;
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
  ],
})
export class PageMenuComponent {}
