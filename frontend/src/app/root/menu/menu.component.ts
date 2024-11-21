import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatNavList } from '@angular/material/list';
import { DividerComponent } from '@app/components/shared';
import { MenuItemComponent } from './menu-item.component';
import { MenuTestActionsComponent } from './menu-test-actions.component';
import { MenuTestLinksComponent } from './menu-test-links.component';

@Component({
  selector: 'kpn-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-nav-list>
      <kpn-menu-item label="Search" icon="search" link="search" />
      <kpn-menu-item label="Plan a route" icon="explore" link="planner" />
      <kpn-menu-item label="Map configuration" icon="layers" link="configuration" />
      <kpn-menu-item label="Settings" icon="settings" link="configuration" />
      <kpn-menu-item label="Analysis" icon="stethoscope" link="analysis" />
      <kpn-menu-item label="Monitor" icon="cardiology" link="monitor" />
    </mat-nav-list>

    <kpn-divider />
    <kpn-menu-test-links />
    <kpn-divider />
    <kpn-menu-test-actions />
    <kpn-divider />
  `,
  standalone: true,
  imports: [
    MatNavList,
    MenuItemComponent,
    DividerComponent,
    MenuTestLinksComponent,
    MenuTestActionsComponent,
  ],
})
export class MenuComponent {}
