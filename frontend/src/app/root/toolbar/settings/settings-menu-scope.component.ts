import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { State } from '@app/state';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';

@Component({
  selector: 'kpn-settings-menu-scope',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div (click)="$event.stopPropagation()">
      <li nz-menu-item>
        <label
          nz-checkbox
          [nzChecked]="scopeInternational()"
          (nzCheckedChange)="toggleScopeInternational()"
        >
          International
        </label>
      </li>

      <li nz-menu-item>
        <label nz-checkbox [nzChecked]="scopeNational()" (nzCheckedChange)="toggleScopeNational()">
          National
        </label>
      </li>

      <li nz-menu-item>
        <label nz-checkbox [nzChecked]="scopeRegional()" (nzCheckedChange)="toggleScopeRegional()">
          Regional
        </label>
      </li>

      <li nz-menu-item>
        <label nz-checkbox [nzChecked]="scopeLocal()" (nzCheckedChange)="toggleScopeLocal()">
          Local
        </label>
      </li>

      <li nz-menu-item>
        <label
          nz-checkbox
          [nzChecked]="scopeNodeRoutes()"
          (nzCheckedChange)="toggleScopeNodeRoutes()"
        >
          Node routes
        </label>
      </li>
    </div>
  `,
  imports: [NzMenuItemComponent, NzCheckboxComponent],
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
