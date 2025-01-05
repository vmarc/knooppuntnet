import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuItem } from '@angular/material/menu';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { DividerComponent } from '@app/components/shared';
import { SettingsMenuPoiComponent } from './settings-menu-poi.component';
import { SettingsMenuLayersComponent } from './settings-menu-layers.component';
import { SettingsMenuMapOptionsComponent } from './settings-menu-map-options.component';
import { SettingsMenuScopeComponent } from './settings-menu-scope.component';

@Component({
  selector: 'kpn-settings-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button [matMenuTriggerFor]="settingsMenu" mat-icon-button>
      <span class="menu-button-icons">
        <mat-icon>settings</mat-icon>
      </span>
    </button>

    <mat-menu #settingsMenu="matMenu">
      <button mat-menu-item [matMenuTriggerFor]="mapOptionsMenu">
        <mat-icon>settings</mat-icon>
        <mat-label>Map options</mat-label>
      </button>
      <button mat-menu-item [matMenuTriggerFor]="scopeMenu">
        <mat-icon>clear_all</mat-icon>
        <mat-label>Scope</mat-label>
      </button>
      <button mat-menu-item [matMenuTriggerFor]="layersMenu">
        <mat-icon>layers</mat-icon>
        <mat-label>Layers</mat-label>
      </button>
    </mat-menu>

    <mat-menu #mapOptionsMenu="matMenu">
      <kpn-settings-menu-map-options />
    </mat-menu>

    <mat-menu #scopeMenu="matMenu">
      <kpn-settings-menu-scope />
    </mat-menu>

    <mat-menu #layersMenu="matMenu">
      <kpn-settings-menu-layers />
      <kpn-divider />
      <kpn-settings-menu-poi />
    </mat-menu>
  `,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatMenu,
    MatMenuTrigger,
    MatLabel,
    MatMenuItem,
    SettingsMenuMapOptionsComponent,
    SettingsMenuScopeComponent,
    SettingsMenuLayersComponent,
    SettingsMenuPoiComponent,
    DividerComponent,
  ],
})
export class SettingsMenuComponent {}
