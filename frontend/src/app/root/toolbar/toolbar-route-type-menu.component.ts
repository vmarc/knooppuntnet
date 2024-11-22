import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { NetworkType } from '@api/custom';
import { State } from '@app/state';
import { ToolbarRouteTypeMenuItemComponent } from './toolbar-route-type-menu-item.component';

@Component({
  selector: 'kpn-toolbar-route-type-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button [matMenuTriggerFor]="menu" aria-label="Select route type" class="menu-button">
      <span class="menu-button-icons">
        <mat-icon>{{ icon() }}</mat-icon>
        <mat-icon>arrow_drop_down</mat-icon>
      </span>
    </button>

    <mat-menu #menu="matMenu">
      <kpn-toolbar-route-type-menu-item
        [networkType]="networkType.hiking"
        i18n-label="@@network-type.hiking"
        label="Hiking"
      />
      <kpn-toolbar-route-type-menu-item
        [networkType]="networkType.cycling"
        i18n-label="@@network-type.cycling"
        label="Cycling"
      />
      <kpn-toolbar-route-type-menu-item
        [networkType]="networkType.horseRiding"
        i18n-label="@@network-type.horseRiding"
        label="Horse riding"
      />
      <kpn-toolbar-route-type-menu-item
        [networkType]="networkType.motorboat"
        i18n-label="@@network-type.motorboat"
        label="Motorboat"
      />
      <kpn-toolbar-route-type-menu-item
        [networkType]="networkType.canoe"
        i18n-label="@@network-type.canoe"
        label="Canoe"
      />
      <kpn-toolbar-route-type-menu-item
        [networkType]="networkType.inlineSkating"
        i18n-label="@@network-type.inlineSkating"
        label="Inline skating"
      />
    </mat-menu>
  `,
  styles: `
    .menu-button {
      border: 0;
      background-color: transparent;
      height: 42px;
    }

    .menu-button:hover {
      border: 0;
      background-color: #eee;
    }

    .menu-button-icons {
      display: flex;
      align-items: center;
    }
  `,
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatMenu,
    MatMenuTrigger,
    ToolbarRouteTypeMenuItemComponent,
  ],
})
export class ToolbarRouteTypeMenuComponent {
  private readonly state = inject(State);
  readonly networkType = NetworkType;
  readonly icon = computed(() => {
    switch (this.state.page.networkType()) {
      case NetworkType.cycling:
        return 'directions_bike';
      case NetworkType.hiking:
        return 'directions_walk';
      case NetworkType.horseRiding:
        return 'bedroom_baby';
      case NetworkType.motorboat:
        return 'directions_boat';
      case NetworkType.canoe:
        return 'kayaking';
      case NetworkType.inlineSkating:
        return 'roller_skating';
      default:
        return '';
    }
  });
}
