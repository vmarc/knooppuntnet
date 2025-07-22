import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDeviationInfo } from '@api/common/monitor/monitor-route-deviation-info';
import { MonitorRouteDeviationListHeaderComponent } from './monitor-route-deviation-list-header.component';
import { MonitorRouteDeviationListItemComponent } from './monitor-route-deviation-list-item.component';
import { MonitorRouteDeviationPopupComponent } from './monitor-route-deviation-popup.component';
import { MonitorRouteDeviationsPageService } from './monitor-route-deviations-page.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { NavService } from '@app/shared/components/nav.service';
import { NzContextMenuService } from 'ng-zorro-antd/dropdown';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';

@Component({
  selector: 'ui-monitor-route-deviation-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-monitor-route-deviation-list-header />

    <ui-list [filter]="false">
      @for (deviation of deviations(); track deviation.id) {
        @defer (on viewport) {
          <ui-list-item [clickable]="true" (click)="selectDeviation(deviation)">
            <ui-monitor-route-deviation-list-item
              [deviation]="deviation"
              (popup)="popupMenu($event.event, $event.deviation, menu)"
            />
          </ui-list-item>
        } @placeholder {
          <div class="deviation-placeholder" aria-hidden="true"></div>
        }
      }
    </ui-list>

    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ui-monitor-route-deviation-popup [deviation]="selectedDeviation()" />
    </nz-dropdown-menu>
  `,
  styleUrl: './monitor-route-deviation-list.scss',
  providers: [MonitorRouteDeviationsPageService, NavService],
  imports: [
    ListComponent,
    ListItemComponent,
    NzDropdownMenuComponent,
    MonitorRouteDeviationPopupComponent,
    MonitorRouteDeviationListItemComponent,
    MonitorRouteDeviationListHeaderComponent,
  ],
})
export class MonitorRouteDeviationListComponent {
  private readonly nzContextMenuService = inject(NzContextMenuService);

  readonly deviations = input.required<MonitorRouteDeviationInfo[]>();
  readonly selectionChange = output<MonitorRouteDeviationInfo>();

  protected readonly selectedDeviation = signal<MonitorRouteDeviationInfo>(undefined);

  popupMenu(
    event: MouseEvent,
    deviation: MonitorRouteDeviationInfo,
    menu: NzDropdownMenuComponent
  ): void {
    event.stopPropagation();
    event.preventDefault();
    this.selectedDeviation.set(deviation);
    this.nzContextMenuService.create(event, menu);
  }

  selectDeviation(deviation: MonitorRouteDeviationInfo): void {
    this.selectionChange.emit(deviation);
  }
}
