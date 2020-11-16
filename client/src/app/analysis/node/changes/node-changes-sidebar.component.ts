import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {NodeChangesService} from './node-changes.service';

@Component({
  selector: 'kpn-node-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="nodeChangesService.filterOptions$ | async"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class NodeChangesSidebarComponent {
  constructor(public nodeChangesService: NodeChangesService) {
  }
}
