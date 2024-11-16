import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuItem } from '@angular/material/menu';
import { MatLabel } from '@angular/material/select';
import { NetworkType } from '@api/custom';
import { RootService } from '../root.service';
import { RouteTypeIconItemComponent } from './route-type-icon.component';

@Component({
  selector: 'kpn-toolbar-route-type-menu-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-menu-item (click)="clicked()">
      <div class="item">
        <kpn-route-type-icon [networkType]="networkType()" />
        <mat-label>{{ title() }}</mat-label>
      </div>
    </button>
  `,
  styles: [
    `
      .item {
        display: flex;
        align-items: center;
      }
    `,
  ],
  standalone: true,
  imports: [MatButtonModule, MatIconModule, MatMenuItem, MatLabel, RouteTypeIconItemComponent],
})
export class ToolbarRouteTypeMenuItemComponent {
  private readonly rootService = inject(RootService);

  readonly networkType = input.required<NetworkType>();
  readonly title = input.required<string>();

  clicked(): void {
    this.rootService.updateNetworkType(this.networkType());
  }
}
