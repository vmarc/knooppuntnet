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
    <div class="tryout-tabs">
      <mat-tab-group
        mat-stretch-tabs="false"
        mat-align-tabs="start"
        fitInkBarToContent="true"
        disablePagination="true"
        class="tryout-tabs"
      >
        <mat-tab>
          <ng-template mat-tab-label>
            <span class="tryout-tabs-main-menu-option">Nodes</span>
            <span class="kpn-brackets">123</span>
          </ng-template>
          <div class="kpn-small-spacer-above kpn-small-spacer-below">Node page content</div>
        </mat-tab>
        <mat-tab>
          <ng-template mat-tab-label>
            <span class="tryout-tabs-main-menu-option">Routes</span>
            <span class="kpn-brackets">77</span>
          </ng-template>
          <div class="kpn-small-spacer-above kpn-small-spacer-below">Route page content</div>
        </mat-tab>
        <mat-tab>
          <ng-template mat-tab-label>
            <span class="tryout-tabs-main-menu-option">Facts</span>
            <span class="kpn-brackets">222</span>
          </ng-template>
          <div class="kpn-small-spacer-above kpn-small-spacer-below">Fact page content</div>
        </mat-tab>
        <mat-tab>
          <ng-template mat-tab-label>
            <span>Map</span>
          </ng-template>
          <div class="kpn-small-spacer-above kpn-small-spacer-below">Map page content</div>
        </mat-tab>
        <mat-tab>
          <ng-template mat-tab-label>
            <span>Changes</span>
          </ng-template>
          <div class="kpn-small-spacer-above kpn-small-spacer-below">Changes page content</div>
        </mat-tab>
      </mat-tab-group>
    </div>
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
