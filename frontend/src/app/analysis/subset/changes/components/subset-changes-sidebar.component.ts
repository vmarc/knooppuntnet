import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ChangeOption } from '@app/kpn/common';
import { SubsetChangesStore } from '../subset-changes.store';

@Component({
  selector: 'kpn-subset-changes-sidebar',
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, ChangeFilterComponent, AsyncPipe],
})
export class SubsetChangesSidebarComponent {
  private readonly store = inject(SubsetChangesStore);
  protected readonly filterOptions = this.store.filterOptions;

  onOptionSelected(option: ChangeOption): void {
    this.store.updateFilterOption(option);
  }
}
