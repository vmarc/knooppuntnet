import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { State } from '@app/state/state';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { MenuTestActionsComponent } from './menu-test-actions.component';
import { MenuTestLinksComponent } from './menu-test-links.component';

@Component({
  selector: 'ui-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="main-menu">
      <div>
        <a routerLink="explore">
          <nz-icon nzType="search" />
          <span>Explore</span>
        </a>
      </div>
      <div>
        <a routerLink="planner">
          <nz-icon nzType="compass" />
          <span>Plan a route</span>
        </a>
      </div>
      <div>
        <a routerLink="analysis">
          <nz-icon nzType="experiment" />
          <span> Analysis </span>
        </a>
      </div>
      <div>
        <a routerLink="monitor">
          <nz-icon nzType="dashboard" />
          <span> Monitor </span>
        </a>
      </div>
    </div>

    <nz-divider />
    <ui-menu-test-links />
    <nz-divider />
    <ui-menu-test-actions />
    <nz-divider />
  `,
  styles: `
    .main-menu {
      padding-top: 1em;
      padding-left: 1em;

      > div {
        display: block;
        padding: 0.5em;
      }

      > div > a > span {
        padding-left: 0.5em;
      }
    }
  `,
  imports: [
    MenuTestActionsComponent,
    MenuTestLinksComponent,
    NzDividerComponent,
    NzIconDirective,
    RouterLink,
  ],
})
export class MenuComponent implements OnInit {
  private readonly state = inject(State);

  ngOnInit(): void {
    this.state.map.updateSubject('explore');
  }
}
