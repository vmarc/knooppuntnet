import {Component} from '@angular/core';
import {SubsetChangesService} from './subset-changes.service';

@Component({
  selector: 'kpn-subset-changes-sidebar',
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="subsetChangesService.filterOptions$ | async"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class SubsetChangesSidebarComponent {
  constructor(public subsetChangesService: SubsetChangesService) {
  }
}
