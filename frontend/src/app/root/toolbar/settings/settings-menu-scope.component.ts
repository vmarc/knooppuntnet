import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { State } from '@app/state';
import { TuiDataListComponent } from '@taiga-ui/core';
import { TuiDataListDropdownManager } from '@taiga-ui/kit';
import { MenuItemCheckboxComponent } from './menu-item-checkbox.component';

@Component({
  selector: 'kpn-settings-menu-scope',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-menu-items">
      <tui-data-list tuiDataListDropdownManager size="m">
        <kpn-menu-item-checkbox
          [value]="scopeInternational()"
          (toggle)="toggleScopeInternational()"
          label="International"
        />
        <kpn-menu-item-checkbox
          [value]="scopeNational()"
          (toggle)="toggleScopeNational()"
          label="National"
        />
        <kpn-menu-item-checkbox
          [value]="scopeRegional()"
          (toggle)="toggleScopeRegional()"
          label="Regional"
        />
        <kpn-menu-item-checkbox
          [value]="scopeLocal()"
          (toggle)="toggleScopeLocal()"
          label="Local"
        />
        <kpn-menu-item-checkbox
          [value]="scopeNodeRoutes()"
          (toggle)="toggleScopeNodeRoutes()"
          label="Node routes"
        />
      </tui-data-list>
    </div>
  `,
  imports: [MenuItemCheckboxComponent, TuiDataListDropdownManager, TuiDataListComponent],
})
export class SettingsMenuScopeComponent {
  private readonly state = inject(State);
  private readonly scopes = this.state.map.scopes;
  readonly scopeInternational = this.scopes.scopeInternational;
  readonly scopeNational = this.scopes.scopeNational;
  readonly scopeRegional = this.scopes.scopeRegional;
  readonly scopeLocal = this.scopes.scopeLocal;
  readonly scopeNodeRoutes = this.scopes.scopeNodeRoutes;

  toggleScopeInternational(): void {
    this.scopes.updateScopeInternational(!this.scopeInternational());
  }

  toggleScopeNational(): void {
    this.scopes.updateScopeNational(!this.scopeNational());
  }

  toggleScopeRegional(): void {
    this.scopes.updateScopeRegional(!this.scopeRegional());
  }

  toggleScopeLocal(): void {
    this.scopes.updateScopeLocal(!this.scopeLocal());
  }

  toggleScopeNodeRoutes(): void {
    this.scopes.updateScopeNodeRoutes(!this.scopeNodeRoutes());
  }
}
