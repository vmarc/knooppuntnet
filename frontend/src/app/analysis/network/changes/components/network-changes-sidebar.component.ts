import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ChangeOption } from '@app/kpn/common';
import { NetworkChangesPageService } from '../network-changes-page.service';

@Component({
  selector: 'kpn-network-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [ChangeFilterComponent, SidebarComponent],
})
export class NetworkChangesSidebarComponent {
  private readonly service = inject(NetworkChangesPageService);
  protected readonly filterOptions = this.service.filterOptions;

  onOptionSelected(option: ChangeOption): void {
    this.service.setFilterOption(option);
  }
}
