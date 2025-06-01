import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { PageComponent } from '@app/shared/components/page/page.component';
import { State } from '@app/state/state';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { MenuTestActionsComponent } from './menu-test-actions.component';
import { MenuTestLinksComponent } from './menu-test-links.component';

@Component({
  selector: 'ui-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
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
    </ui-page>
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
    BreadcrumbComponent,
    PageComponent,
  ],
})
export class MenuComponent implements OnInit {
  private readonly state = inject(State);

  protected readonly breadcrumbItems: BreadcrumbItem[] = [{ label: Breadcrumbs.homeLabel }];

  ngOnInit(): void {
    this.state.map.updateSubject('explore');
  }
}
