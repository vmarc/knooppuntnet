import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ChangeOption } from '@app/kpn/common';
import { RouterService } from '../../../../shared/services/router.service';
import { NodeChangesStore } from '../node-changes.store';

@Component({
  selector: 'kpn-node-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
  providers: [NodeChangesStore, RouterService],
  standalone: true,
  imports: [SidebarComponent, ChangeFilterComponent],
})
export class NodeChangesSidebarComponent {
  private readonly store = inject(NodeChangesStore);
  protected readonly filterOptions = this.store.filterOptions;

  onOptionSelected(option: ChangeOption): void {
    this.store.updateFilterOption(option);
  }
}
