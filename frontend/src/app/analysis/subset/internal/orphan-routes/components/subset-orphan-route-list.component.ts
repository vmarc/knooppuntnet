import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { FilterComponent } from '@app/analysis/components/filter/filter.component';
import { SubsetOrphanRouteListItemComponent } from './subset-orphan-route-list-item.component';
import { EditService } from '@app/shared/components/edit.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { SubsetOrphanRoutesPageService } from '../subset-orphan-routes-page.service';

@Component({
  selector: 'ui-subset-orphan-route-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-list
      [pageIndex]="pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount()"
      [filter]="true"
    >
      <ui-filter [filterOptions]="filterOptions()" filter />
      <ui-edit-link
        header-extra
        (edit)="edit()"
        i18n-title="@@network-routes.edit.title"
        title="Load the routes in this page in JOSM"
      />

      @for (route of pageRoutes(); track route.id) {
        <ui-list-item [selected]="false">
          <ui-subset-orphan-route-list-item
            [routeType]="routeType()"
            [rowNumber]="rowNumber($index)"
            [row]="route"
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
    SubsetOrphanRouteListItemComponent,
  ],
})
export class SubsetOrphanRouteListComponent {
  private readonly editService = inject(EditService);
  private readonly service = inject(SubsetOrphanRoutesPageService);
  protected readonly pageSize = this.service.pageSize;
  protected readonly pageIndex = this.service.pageIndex;
  protected readonly routeType = this.service.routeType;
  protected readonly routes = this.service.filteredRoutes;
  protected readonly filterOptions = this.service.filterOptions;
  protected readonly pageRoutes = this.service.pageRoutes;
  protected readonly routeCount = this.service.routeCount;

  rowNumber(index: number): number {
    return this.pageSize() * this.pageIndex() + index + 1;
  }

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number) {
    this.service.updatePageIndex(pageIndex);
  }

  edit(): void {
    const routeIds = this.routes().map((orphanRoute) => orphanRoute.id);
    this.editService.edit({
      relationIds: routeIds,
      fullRelation: true,
    });
  }
}
