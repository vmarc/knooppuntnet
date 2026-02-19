import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RoutePath } from '@api/common/route/route-path';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { RoutePathListItemComponent } from '@app/shared/components/route/paths/route-path-list-item.component';

@Component({
  selector: 'ui-route-path-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-list>
      @for (path of paths(); track path.id) {
        <ui-list-item [clickable]="true" (click)="selectPath(path)">
          <ui-route-path-list-item [path]="path" />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [ListComponent, ListItemComponent, RoutePathListItemComponent],
})
export class RoutePathListComponent {
  readonly paths = input.required<ReadonlyArray<RoutePath>>();

  readonly selectChange = output<RoutePath>();

  selectPath(path: RoutePath): void {
    this.selectChange.emit(path);
  }
}
