import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DividerComponent } from '@app/components/shared';
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
      <kpn-divider />
      <kpn-tryout-tabs-drop-down />
    </kpn-old-page>
  `,
  imports: [
    DividerComponent,
    OldPageComponent,
    TryoutTabsDropDownComponent,
    TryoutTabsMenuComponent,
  ],
})
export class TryoutTabsPageComponent {}
