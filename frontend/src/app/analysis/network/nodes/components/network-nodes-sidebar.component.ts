import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { NetworkNodesPageService } from '../network-nodes-page.service';

@Component({
  selector: 'kpn-network-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="service.filterOptions()" />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, FilterComponent],
})
export class NetworkNodesSidebarComponent {
  protected readonly service = inject(NetworkNodesPageService);
}
