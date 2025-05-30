import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TimeInfo } from '@api/common/time-info';
import { RouteScope } from '@api/common/route-scope';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { LocationNodesFilterComponent } from '@app/analysis/location/internal/nodes/components/location-nodes-filter.component';
import { DayComponent } from '@app/shared/components/day/day.component';
import { EditService } from '@app/shared/components/edit.service';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';
import { LocationNodesPageService } from '../location-nodes-page.service';
import { LocationNodeAnalysisComponent } from './location-node-analysis.component';
import { LocationNodeRoutesComponent } from './location-node-routes.component';

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
          <div class="kpn-line">
            <span>{{ node.rowIndex + 1 }}</span>
            <ui-action-button-node [nodeId]="node.id" />
            <ui-link-node [nodeId]="node.id" [nodeName]="node.name" />
            <span>{{ node.longName }}</span>
          </div>
          <div class="kpn-line">
            <ui-location-node-analysis
              [node]="node"
              [routeType]="routeType()"
              [routeScope]="routeScope"
            />
            @if (node.lastSurvey) {
              <span>
                <span i18n="@@location-nodes.table.last-survey" class="kpn-label">Survey</span>
                <span>
                  {{ node.lastSurvey | day }}
                </span>
              </span>
            }
            <span>
              <span i18n="@@location-nodes.table.last-edit" class="kpn-label">Last edit</span>
              <span>
                <ui-day [timestamp]="node.lastUpdated" />
              </span>
            </span>
          </div>
          <div>
            <span>
              <span i18n="@@location-nodes.table.expected-route-count" class="kpn-label">
                Expected
              </span>
              <span>{{ node.expectedRouteCount }}</span>
            </span>
          </div>

          <div>
            <ui-location-node-routes [node]="node" />
          </div>
          <td></td>
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [
    ActionButtonNodeComponent,
    DayComponent,
    DayPipe,
    DayPipe,
    EditLinkComponent,
    LinkNodeComponent,
    ListComponent,
    ListItemComponent,
    LocationNodeAnalysisComponent,
    LocationNodeRoutesComponent,
    LocationNodesFilterComponent,
  ],
})
export class LocationNodeListComponent {
  private readonly service = inject(LocationNodesPageService);
  private readonly editService = inject(EditService);

  timeInfo = input.required<TimeInfo>();
  nodes = input.required<LocationNodeInfo[]>();
  nodeCount = input.required<number>();

  readonly pageSize = this.service.pageSize;
  readonly pageIndex = this.service.pageIndex;
  readonly routeType = this.service.routeType;

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
