import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { FilterComponent } from '@app/analysis/components/filter/filter.component';
import { EditService } from '@app/shared/components/edit.service';
import { NetworkNodeListItemComponent } from './network-node-list-item.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { NetworkNodesPageService } from '../network-nodes-page.service';

@Component({
  selector: 'ui-network-node-list',
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
      <ui-filter [filterOptions]="filterOptions()" filter />

      <ui-edit-link
        header-extra
        (edit)="edit()"
        i18n-title="@@network-nodes.edit.title"
        title="Load the nodes in this page in JOSM"
      />

      @for (node of pageNodes(); track node.detail.id) {
        <ui-list-item [selected]="false">
          <ui-network-node-list-item
            [routeType]="routeType()"
            [routeScope]="routeScope()"
            [rowNumber]="rowNumber($index)"
            [row]="node"
          />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [
    EditLinkComponent,
    FilterComponent,
    ListComponent,
    ListItemComponent,
    NetworkNodeListItemComponent,
  ],
})
export class NetworkNodeListComponent {
  private readonly service = inject(NetworkNodesPageService);
  private readonly editService = inject(EditService);

  readonly routeType = input.required<RouteType>();
  readonly routeScope = input.required<RouteScope>();
  readonly timeInfo = input.required<TimeInfo>();
  readonly surveyDateInfo = input.required<SurveyDateInfo>();
  readonly nodes = input.required<ReadonlyArray<NetworkNodeRow>>();

  protected readonly pageIndex = this.service.pageIndex;
  protected readonly pageSize = this.service.pageSize;

  protected readonly filteredNodes = this.service.filteredNodes;
  protected readonly nodeCount = computed(() => this.filteredNodes()?.length);
  protected readonly filterOptions = this.service.filterOptions;
  protected readonly pageNodes = this.service.pageNodes;

  rowNumber(index: number): number {
    return this.pageSize() * this.pageIndex() + index + 1;
  }

  onPageIndexChange(pageIndex: number) {
    this.service.updatePageIndex(pageIndex);
  }

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  edit(): void {
    const nodeIds = this.filteredNodes().map((node) => node.detail.id);
    this.editService.edit({
      nodeIds,
    });
  }
}
