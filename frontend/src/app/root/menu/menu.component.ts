import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DividerComponent } from '@app/components/shared';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { MenuTestActionsComponent } from './menu-test-actions.component';
import { MenuTestLinksComponent } from './menu-test-links.component';

@Component({
  selector: 'kpn-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="main-menu">
      <a routerLink="explore">
        <nz-icon nzType="search" />
        <span>Explore</span>
      </a>
      <a routerLink="planner">
        <nz-icon nzType="compass" />
        <span>Plan a route</span>
      </a>
      <a routerLink="analysis">
        <nz-icon nzType="experiment" />
        <span> Analysis </span>
      </a>
      <a routerLink="monitor">
        <nz-icon nzType="dashboard" />
        <span> Monitor </span>
      </a>
    </div>

    <kpn-divider />
    <kpn-menu-test-links />
    <kpn-divider />
    <kpn-menu-test-actions />
    <kpn-divider />
  `,
  styles: `
    .main-menu {
      padding-top: 2em;
      padding-left: 1em;

      > a {
        display: block;
        padding: 0.5em;
      }

      > a > span {
        padding-left: 0.5em;
      }
    }
  `,
  imports: [
    DividerComponent,
    MenuTestActionsComponent,
    MenuTestLinksComponent,
    NzIconDirective,
    RouterLink,
  ],
})
export class MenuComponent {}
