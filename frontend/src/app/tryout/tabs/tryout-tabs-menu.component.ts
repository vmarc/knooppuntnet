import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDivider } from '@angular/material/divider';
import { MatTabLabel } from '@angular/material/tabs';
import { MatTabGroup } from '@angular/material/tabs';
import { MatTab } from '@angular/material/tabs';
import { PageComponent } from '@app/components/shared/page';
import { TryoutTabsDropDownComponent } from './tryout-tabs-drop-down.component';

@Component({
  selector: 'kpn-tryout-tabs-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-tab-group mat-stretch-tabs="false" mat-align-tabs="start">
      <mat-tab>
        <ng-template mat-tab-label>
          <span class="menu-link">Nodes</span>&nbsp;<span class="menu-count">(123)</span>
        </ng-template>

        Content 1
      </mat-tab>
      <mat-tab label="Routes (45)"> Content 2</mat-tab>
      <mat-tab label="Facts (222)"> Content 3</mat-tab>
      <mat-tab label="Map"> Content 3</mat-tab>
      <mat-tab label="Changes"> Content 3</mat-tab>
    </mat-tab-group>
  `,
  styles: `
    .menu-link {
      color: blue;
      font-size: 16px;
      font-family: Roboto, Helvetica, Arial, sans-serif;
    }

    .menu-count {
      color: rgb(128, 128, 128);
    }
  `,
  standalone: true,
  imports: [
    MatTabGroup,
    MatTab,
    MatTabLabel,
    PageComponent,
    TryoutTabsDropDownComponent,
    MatDivider,
  ],
})
export class TryoutTabsMenuComponent {}
