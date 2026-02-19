import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkRouteRow } from '@api/common/network/network-route-row';
import { RouteType } from '@api/common/route-type';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { FilterComponent } from '@app/analysis/components/filter/filter.component';
import { NetworkRouteListItemComponent } from '@app/analysis/network/internal/routes/components/network-route-list-item.component';
import { EditService } from '@app/shared/components/edit.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { NetworkRoutesPageService } from '../network-routes-page.service';

@Component({
  selector: 'ui-network-route-list',
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
          <ui-network-route-list-item
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
    NetworkRouteListItemComponent,
  ],
})
export class NetworkRouteListComponent {
  readonly timeInfo = input.required<TimeInfo>();
  readonly surveyDateInfo = input.required<SurveyDateInfo>();
  readonly routeType = input.required<RouteType>();
  readonly routes = input.required<ReadonlyArray<NetworkRouteRow>>();

  private readonly editService = inject(EditService);
  private readonly service = inject(NetworkRoutesPageService);
  protected readonly filteredRoutes = this.service.filteredRoutes;
  protected readonly filterOptions = this.service.filterOptions;

  protected readonly routeCount = computed(() => this.filteredRoutes()?.length);
  protected readonly pageSize = this.service.pageSize;
  protected readonly pageIndex = signal<number>(0);
  protected readonly pageRoutes = computed(() => {
    const pageIndex = this.pageIndex();
    const pageSize = this.pageSize();
    const start = pageIndex * pageSize;
    const end = start + pageSize;
    return this.filteredRoutes()?.slice(start, end);
  });

  rowNumber(index: number): number {
    return this.pageSize() * this.pageIndex() + index + 1;
  }

  onPageSizeChange(pageSize: number): void {
    this.pageIndex.set(0);
    this.service.updatePageSize(pageSize);
  }

  edit(): void {
    const relationIds = this.routes().map((route) => route.id);
    this.editService.edit({
      relationIds,
      fullRelation: true,
    });
  }

  onPageIndexChange(event: number) {
    this.pageIndex.set(event);
  }
}
