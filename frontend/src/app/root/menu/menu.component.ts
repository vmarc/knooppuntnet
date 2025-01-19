import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DividerComponent } from '@app/components/shared';
import { TuiLink } from '@taiga-ui/core';
import { MenuTestActionsComponent } from './menu-test-actions.component';
import { MenuTestLinksComponent } from './menu-test-links.component';

@Component({
  selector: 'kpn-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="main-menu">
      <div>
        <a tuiLink iconStart="@tui.search" routerLink="explore">Explore</a>
      </div>
      <div>
        <a tuiLink iconStart="@tui.compass" routerLink="planner">Plan a route</a>
      </div>
      <div>
        <a tuiLink iconStart="@tui.stethoscope" routerLink="analysis">Analysis</a>
      </div>
      <div>
        <a tuiLink iconStart="@tui.heart-pulse" routerLink="monitor">Monitor</a>
      </div>
    </div>

    <kpn-divider />
    <kpn-menu-test-links />
    <kpn-divider />
    <kpn-menu-test-actions />
    <kpn-divider />
  `,
  styles: `
    .main-menu {
      font-size: 1.2em;
      padding-top: 1em;
      padding-left: 1em;

      > div {
        padding: 0.5em;
      }
    }
  `,
  imports: [
    DividerComponent,
    MenuTestActionsComponent,
    MenuTestLinksComponent,
    RouterLink,
    TuiLink,
  ],
})
export class MenuComponent {}
