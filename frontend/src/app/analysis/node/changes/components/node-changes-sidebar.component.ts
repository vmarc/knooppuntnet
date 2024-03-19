import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ChangeOption } from '@app/kpn/common';
import { RouterService } from '../../../../shared/services/router.service';
import { NodeChangesPageService } from '../node-changes-page.service';

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
  providers: [NodeChangesPageService, RouterService],
  standalone: true,
  imports: [SidebarComponent, ChangeFilterComponent],
})
export class NodeChangesSidebarComponent {
  private readonly service = inject(NodeChangesPageService);
  protected readonly filterOptions = this.service.filterOptions;

  onOptionSelected(option: ChangeOption): void {
    this.service.updateFilterOption(option);
  }
}
