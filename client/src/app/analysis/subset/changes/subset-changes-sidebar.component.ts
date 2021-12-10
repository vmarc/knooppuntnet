import { Component } from '@angular/core';
import { SubsetChangesService } from './subset-changes.service';

@Component({
  selector: 'kpn-subset-changes-sidebar',
  template: `
    <kpn-sidebar>
      <kpn-subset-analysis-mode></kpn-subset-analysis-mode>
      <kpn-change-filter
        [filterOptions]="subsetChangesService.filterOptions$ | async"
      ></kpn-change-filter>
    </kpn-sidebar>
  `,
})
export class SubsetChangesSidebarComponent {
  constructor(public subsetChangesService: SubsetChangesService) {}
}
