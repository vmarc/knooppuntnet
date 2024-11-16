import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { NetworkType } from '@api/custom';
import { RootService } from '../root.service';
import { RouteTypeIconItemComponent } from './route-type-icon.component';
import { ToolbarRouteTypeMenuItemComponent } from './toolbar-route-type-menu-item.component';

@Component({
  selector: 'kpn-toolbar-route-type-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button [matMenuTriggerFor]="menu" aria-label="Select route type">
      <kpn-route-type-icon [networkType]="rootService.networkType()" />
    </button>

    <mat-menu #menu="matMenu">
      <kpn-toolbar-route-type-menu-item [networkType]="networkType.hiking" title="Hiking" />
      <kpn-toolbar-route-type-menu-item [networkType]="networkType.cycling" title="Cycling" />
      <kpn-toolbar-route-type-menu-item
        [networkType]="networkType.horseRiding"
        title="Horse riding"
      />
      <kpn-toolbar-route-type-menu-item [networkType]="networkType.motorboat" title="Motorboat" />
      <kpn-toolbar-route-type-menu-item [networkType]="networkType.canoe" title="Canoe" />
      <kpn-toolbar-route-type-menu-item
        [networkType]="networkType.inlineSkating"
        title="Inline skating"
      />
    </mat-menu>
  `,
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatMenu,
    MatMenuTrigger,
    ToolbarRouteTypeMenuItemComponent,
    RouteTypeIconItemComponent,
  ],
})
export class ToolbarRouteTypeMenuComponent {
  readonly rootService = inject(RootService);
  networkType = NetworkType;
}
