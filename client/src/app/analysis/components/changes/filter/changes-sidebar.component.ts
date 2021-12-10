import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesService } from './changes.service';

@Component({
  selector: 'kpn-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-mode></kpn-analysis-mode>
      <kpn-change-filter
        [filterOptions]="changesService.filterOptions$ | async"
      ></kpn-change-filter>
    </kpn-sidebar>
  `,
})
export class ChangesSidebarComponent {
  constructor(public changesService: ChangesService) {}
}
