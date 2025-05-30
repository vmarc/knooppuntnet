import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter/filter.component';
import { SidebarComponent } from '@app/shared/components/sidebar/sidebar.component';
import { SubsetOrphanRoutesPageService } from '../subset-orphan-routes-page.service';

@Component({
  selector: 'ui-subset-orphan-routes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-sidebar>
      <ui-filter [filterOptions]="filterOptions()" />
    </ui-sidebar>
  `,
  imports: [SidebarComponent, FilterComponent],
})
export class SubsetOrphanRoutesSidebarComponent {
  private readonly service = inject(SubsetOrphanRoutesPageService);
  protected readonly filterOptions = this.service.filterOptions;
}
