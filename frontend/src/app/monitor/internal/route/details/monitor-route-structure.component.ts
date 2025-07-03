import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { StructureRow } from '@api/common/route/structure-row';
import { MonitorRouteStructureRowComponent } from '@app/monitor/internal/route/details/monitor-route-structure-row.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';

@Component({
  selector: 'ui-monitor-route-structure',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-list>
      @for (structureRow of structureRows(); track structureRow.rowIndex) {
        <ui-list-item>
          <ui-monitor-route-structure-row
            [admin]="admin()"
            [groupName]="groupName()"
            [routeName]="routeName()"
            [structureRow]="structureRow"
            [referenceType]="referenceType()"
          />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [ListComponent, ListItemComponent, MonitorRouteStructureRowComponent],
})
export class MonitorRouteStructureComponent {
  readonly admin = input.required<boolean>();
  readonly groupName = input.required<string>();
  readonly routeName = input.required<string>();
  readonly structureRows = input.required<StructureRow[]>();
  readonly referenceType = input.required<string>();
}
