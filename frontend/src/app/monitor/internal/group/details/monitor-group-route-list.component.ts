import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteDetail } from '@api/common/monitor/monitor-route-detail';
import { MonitorGroupRouteListItemComponent } from '@app/monitor/internal/group/details/monitor-group-route-list-item.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';

@Component({
  selector: 'ui-monitor-group-route-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-list [filter]="false">
      @for (route of routes(); track route.routeId) {
        <ui-list-item [clickable]="true" (click)="selectRoute(route)">
          <ui-monitor-group-route-list-item
            [admin]="admin()"
            [groupName]="groupName()"
            [monitorRouteDetail]="route"
          />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [ListComponent, ListItemComponent, MonitorGroupRouteListItemComponent],
})
export class MonitorGroupRouteListComponent {
  readonly admin = input.required<boolean>();
  readonly groupName = input.required<string>();
  readonly routes = input.required<MonitorRouteDetail[]>();
  readonly selectionChange = output<MonitorRouteDetail>();

  selectRoute(route: MonitorRouteDetail) {
    this.selectionChange.emit(route);
  }
}
