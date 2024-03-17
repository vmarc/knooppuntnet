import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { SubsetOrphanNodesService } from './subset-orphan-nodes.service';

@Component({
  selector: 'kpn-subset-orphan-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="filterOptions$ | async" />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, FilterComponent, AsyncPipe],
})
export class SubsetOrphanNodesSidebarComponent {
  private readonly subsetOrphanNodesService = inject(SubsetOrphanNodesService);
  protected readonly filterOptions$ = this.subsetOrphanNodesService.filterOptions$;
}
