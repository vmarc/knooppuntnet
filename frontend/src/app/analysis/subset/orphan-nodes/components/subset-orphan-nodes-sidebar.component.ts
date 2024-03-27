import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { SubsetOrphanNodesPageService } from '../subset-orphan-nodes-page.service';

@Component({
  selector: 'kpn-subset-orphan-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="filterOptions()" />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, FilterComponent],
})
export class SubsetOrphanNodesSidebarComponent {
  private readonly service = inject(SubsetOrphanNodesPageService);
  protected readonly filterOptions = this.service.filterOptions;
}
