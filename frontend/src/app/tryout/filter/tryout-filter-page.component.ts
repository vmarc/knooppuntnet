import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatLabel } from '@angular/material/form-field';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzSubMenuComponent } from 'ng-zorro-antd/menu';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { NzTagComponent } from 'ng-zorro-antd/tag';

@Component({
  selector: 'kpn-tryout-filter-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-page>
      <h1>Tryout filter</h1>
      <nz-collapse>
        <nz-collapse-panel nzHeader="Filter" nzActive="true">
          <a nz-dropdown [nzDropdownMenu]="filterMenu" class="menu-link">
            Add
            <nz-icon nzType="down" />
          </a>
          @for (filterName of filterNames(); track filterName) {
            <nz-tag nzMode="closeable" (nzOnClose)="remove(filterName)">{{ filterName }}</nz-tag>
          }
        </nz-collapse-panel>
      </nz-collapse>

      <nz-dropdown-menu #filterMenu="nzDropdownMenu">
        <ul nz-menu>
          <li nz-submenu nzTitle="Fact">
            <ul>
              <li nz-menu-item>
                <span>RouteOverlappingWays</span>
                <span class="menu-count">(12)</span>
              </li>
              <li nz-menu-item>
                <span>RouteRedundantNodes</span>
                <span class="menu-count"> (3)</span>
              </li>
              <li nz-menu-item>
                <span>RouteSuspiciousWays</span>
                <span class="menu-count"> (11)</span>
              </li>
              <li nz-menu-item>
                <span>RouteTagInvalid</span>
                <span class="menu-count"> (1)</span>
              </li>
            </ul>
          </li>

          <li nz-submenu nzTitle="Survey">
            <ul>
              <li nz-menu-item (click)="add('surveyed')">
                <span>Surveyed</span>
                <span class="menu-count"> (11)</span>
              </li>
              <li nz-menu-item (click)="add('not-surveyed')">
                <span>Not surveyed</span>
                <span class="menu-count"> (31)</span>
              </li>
            </ul>
          </li>
        </ul>
      </nz-dropdown-menu>

      <div class="kpn-small-spacer-above kpn-small-spacer-below">
        <mat-label>query params</mat-label>
        {{ queryParams() }}
      </div>
    </kpn-page>
    <!-- eslint-enable @angular-eslint/template/i18n -->
  `,
  styles: `
    .menu-link {
      margin-right: 0.5em;
    }

    .menu-count {
      padding-left: 1em;
      color: darkgray;
    }
  `,
  imports: [
    MatLabel,
    NzCollapseComponent,
    NzCollapsePanelComponent,
    NzDropDownDirective,
    NzDropdownMenuComponent,
    NzIconDirective,
    NzMenuDirective,
    NzMenuItemComponent,
    NzSubMenuComponent,
    NzTagComponent,
    PageComponent,
  ],
})
export class TryoutFilterPageComponent {
  readonly filterNames = signal<string[]>([
    'RouteOverlappingWays (12)',
    'RouteRedundantNodes (3)',
    'RouteSuspiciousWays (11)',
    'RouteTagInvalid (1)',
  ]);

  readonly queryParams = computed(() => {
    const params = this.filterNames().join(',');
    return 'filters=' + params;
  });

  remove(filterName: string): void {
    console.log('remove filter ' + filterName);
    const updatedFilterNames = this.filterNames().filter((name) => name !== filterName);
    this.filterNames.set(updatedFilterNames);
  }

  add(filterName: string): void {
    console.log('add filter ' + filterName);
    const updatedFilterNames = [...this.filterNames(), filterName];
    this.filterNames.set(updatedFilterNames);
  }
}
