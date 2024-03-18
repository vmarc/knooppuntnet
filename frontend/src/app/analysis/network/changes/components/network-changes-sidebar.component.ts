import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ChangeOption } from '@app/kpn/common';
import { NetworkChangesStore } from '../network-changes.store';

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
  private readonly store = inject(NetworkChangesStore);
  protected readonly filterOptions = this.store.filterOptions;

  onOptionSelected(option: ChangeOption): void {
    this.store.updateFilterOption(option);
  }
}
