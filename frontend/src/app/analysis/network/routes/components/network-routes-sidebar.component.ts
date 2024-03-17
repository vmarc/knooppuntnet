import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { NetworkRoutesService } from './network-routes.service';

@Component({
  selector: 'kpn-network-routes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="networkRoutesService.filterOptions$ | async" />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, FilterComponent, AsyncPipe],
})
export class NetworkRoutesSidebarComponent {
  protected networkRoutesService = inject(NetworkRoutesService);
}
