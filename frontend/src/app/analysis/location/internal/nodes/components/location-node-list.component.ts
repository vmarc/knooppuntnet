import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TimeInfo } from '@api/common/time-info';
import { RouteScope } from '@api/common/route-scope';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { LocationNodeListItemComponent } from './location-node-list-item.component';
import { LocationNodesFilterComponent } from './location-nodes-filter.component';
import { EditService } from '@app/shared/components/edit.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { LocationNodesPageService } from '../location-nodes-page.service';

@Component({
  selector: 'ui-location-node-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-list
      [pageIndex]="pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="nodeCount()"
      [filter]="true"
    >
      <ui-location-nodes-filter filter />

      <ui-edit-link
        header-extra
        (edit)="edit()"
        i18n-title="@@location-nodes.edit.title"
        title="Load the nodes in this page in JOSM"
      />

      @for (node of nodes(); track node.id) {
        <ui-list-item [selected]="false">
          <ui-location-node-list-item
            [routeType]="routeType()"
            [routeScope]="routeScope"
            [node]="node"
          />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [
    EditLinkComponent,
    ListComponent,
    ListItemComponent,
    LocationNodesFilterComponent,
    LocationNodeListItemComponent,
  ],
})
export class LocationNodeListComponent {
  private readonly service = inject(LocationNodesPageService);
  private readonly editService = inject(EditService);

  readonly timeInfo = input.required<TimeInfo>();
  readonly nodes = input.required<LocationNodeInfo[]>();
  readonly nodeCount = input.required<number>();

  protected readonly pageSize = this.service.pageSize;
  protected readonly pageIndex = this.service.pageIndex;
  protected readonly routeType = this.service.routeType;

  // TODO SIGNAL
  routeScope: RouteScope = 'regional';

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.service.setPageIndex(pageIndex);
  }

  edit(): void {
    const editParameters: EditParameters = {
      nodeIds: this.nodes().map((node) => node.id),
    };
    this.editService.edit(editParameters);
  }
}
