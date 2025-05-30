import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter/change-filter.component';
import { ChangeOption } from '@app/shared/kpn/common/change-option';
import { SidebarComponent } from '@app/shared/components/sidebar/sidebar.component';
import { LocationChangesPageService } from '../location-changes-page.service';

@Component({
  selector: 'ui-location-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-sidebar>
      <ui-change-filter
        [filterOptions]="service.filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />
    </ui-sidebar>
  `,
  imports: [SidebarComponent, ChangeFilterComponent],
})
export class LocationChangesSidebarComponent {
  protected readonly service = inject(LocationChangesPageService);

  onOptionSelected(option: ChangeOption): void {
    this.service.setFilterOption(option);
  }
}
