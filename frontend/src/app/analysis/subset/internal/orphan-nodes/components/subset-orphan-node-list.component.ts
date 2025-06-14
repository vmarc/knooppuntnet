import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { FilterComponent } from '@app/analysis/components/filter/filter.component';
import { SubsetOrphanNodeListItemComponent } from './subset-orphan-node-list-item.component';
import { EditService } from '@app/shared/components/edit.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { NzTableModule } from 'ng-zorro-antd/table';
import { SubsetOrphanNodesPageService } from '../subset-orphan-nodes-page.service';

@Component({
  selector: 'ui-subset-orphan-node-list',
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

      @for (node of pageNodes(); track node.id) {
        <ui-list-item [selected]="false">
          <ui-subset-orphan-node-list-item [rowNumber]="rowNumber($index)" [row]="node" />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [
    EditLinkComponent,
    FilterComponent,
    ListComponent,
    ListItemComponent,
    NzTableModule,
    SubsetOrphanNodeListItemComponent,
  ],
})
export class SubsetOrphanNodeListComponent {
  private readonly editService = inject(EditService);
  private readonly service = inject(SubsetOrphanNodesPageService);
  protected readonly nodes = this.service.filteredNodes;
  protected readonly nodeCount = this.service.nodeCount;
  protected readonly pageIndex = this.service.pageIndex;
  protected readonly pageSize = this.service.pageSize;
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
    const nodeIds = this.nodes().map((node) => node.id);
    this.editService.edit({
      nodeIds,
    });
  }
}
