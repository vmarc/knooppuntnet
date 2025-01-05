import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuItem } from '@angular/material/menu';
import { State } from '@app/state';

@Component({
  selector: 'kpn-settings-menu-scope',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div mat-menu-item>
      <mat-checkbox
        [checked]="scopeInternational()"
        (change)="updateScopeInternational($event)"
        (click)="$event.stopPropagation()"
      >
        International
      </mat-checkbox>
    </div>
    <div mat-menu-item>
      <mat-checkbox
        [checked]="scopeNational()"
        (change)="updateScopeNational($event)"
        (click)="$event.stopPropagation()"
      >
        National
      </mat-checkbox>
    </div>
    <div mat-menu-item>
      <mat-checkbox
        [checked]="scopeRegional()"
        (change)="updateScopeRegional($event)"
        (click)="$event.stopPropagation()"
      >
        Regional
      </mat-checkbox>
    </div>
    <div mat-menu-item>
      <mat-checkbox
        [checked]="scopeLocal()"
        (change)="updateScopeLocal($event)"
        (click)="$event.stopPropagation()"
      >
        Local
      </mat-checkbox>
    </div>
    <div mat-menu-item>
      <mat-checkbox
        [checked]="scopeNodeRoutes()"
        (change)="updateScopeNodeRoutes($event)"
        (click)="$event.stopPropagation()"
      >
        Node routes
      </mat-checkbox>
    </div>
  `,
  styles: ``,
  imports: [MatButtonModule, MatIconModule, MatCheckbox, MatMenuItem],
})
export class SettingsMenuScopeComponent {
  private readonly state = inject(State);
  private readonly scopes = this.state.map.scopes;
  readonly scopeInternational = this.scopes.scopeInternational;
  readonly scopeNational = this.scopes.scopeNational;
  readonly scopeRegional = this.scopes.scopeRegional;
  readonly scopeLocal = this.scopes.scopeLocal;
  readonly scopeNodeRoutes = this.scopes.scopeNodeRoutes;

  updateScopeInternational(event: MatCheckboxChange): void {
    this.scopes.updateScopeInternational(event.checked);
  }

  updateScopeNational(event: MatCheckboxChange): void {
    this.scopes.updateScopeNational(event.checked);
  }

  updateScopeRegional(event: MatCheckboxChange): void {
    this.scopes.updateScopeRegional(event.checked);
  }

  updateScopeLocal(event: MatCheckboxChange): void {
    this.scopes.updateScopeLocal(event.checked);
  }

  updateScopeNodeRoutes(event: MatCheckboxChange): void {
    this.scopes.updateScopeNodeRoutes(event.checked);
  }
}
