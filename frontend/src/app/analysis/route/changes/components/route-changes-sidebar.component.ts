import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ChangeOption } from '@app/kpn/common';
import { RouteChangesPageService } from '../route-changes-page.service';

@Component({
  selector: 'kpn-route-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="service.filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, ChangeFilterComponent, AsyncPipe],
})
export class RouteChangesSidebarComponent {
  protected readonly service = inject(RouteChangesPageService);

  onOptionSelected(option: ChangeOption): void {
    this.service.updateFilterOption(option);
  }
}
