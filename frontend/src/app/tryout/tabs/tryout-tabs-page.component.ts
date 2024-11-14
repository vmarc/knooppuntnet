import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDivider } from '@angular/material/divider';
import { OldPageComponent } from '@app/components/shared/page';
import { TryoutTabsDropDownComponent } from './tryout-tabs-drop-down.component';
import { TryoutTabsMenuComponent } from './tryout-tabs-menu.component';

@Component({
  selector: 'kpn-tryout-tabs-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <h1>Tryout tabs</h1>
      <kpn-tryout-tabs-menu />
      <div class="kpn-small-spacer-above kpn-small-spacer-below">
        <mat-divider />
      </div>
      <kpn-tryout-tabs-drop-down />
    </kpn-old-page>
  `,
  standalone: true,
  imports: [OldPageComponent, TryoutTabsDropDownComponent, MatDivider, TryoutTabsMenuComponent],
})
export class TryoutTabsPageComponent {}
