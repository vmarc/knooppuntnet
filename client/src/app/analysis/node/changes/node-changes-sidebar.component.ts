import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { NodeChangesService } from './node-changes.service';

@Component({
  selector: 'kpn-node-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="filterOptions"></kpn-change-filter>
    </kpn-sidebar>
  `,
})
export class NodeChangesSidebarComponent {
  // TODO nodeChangesService.filterOptions$ | async
  filterOptions: ChangesFilterOption[] = [];
  constructor(public nodeChangesService: NodeChangesService) {}
}
