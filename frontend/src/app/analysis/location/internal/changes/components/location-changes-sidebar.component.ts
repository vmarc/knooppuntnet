import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { ChangeOption } from '@app/shared/kpn/common/change-option';
import { SidebarComponent } from '@app/shared/components/sidebar/sidebar.component';
import { LocationChangesPageService } from '../location-changes-page.service';

@Component({
  selector: 'kpn-location-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="service.filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
  imports: [SidebarComponent, ChangeFilterComponent],
})
export class LocationChangesSidebarComponent {
  protected readonly service = inject(LocationChangesPageService);

  onOptionSelected(option: ChangeOption): void {
    this.service.setFilterOption(option);
  }
}
