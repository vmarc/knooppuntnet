import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TimeInfo } from '@api/common/time-info';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { LocationRouteListItemComponent } from './location-route-list-item.component';
import { LocationRoutesFilterComponent } from './location-routes-filter.component';
import { EditService } from '@app/shared/components/edit.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { LocationRoutesPageService } from '../location-routes-page.service';

@Component({
  selector: 'ui-location-route-list',
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
      <ui-location-routes-filter filter />
      <ui-edit-link
        header-extra
        (edit)="edit()"
        i18n-title="@@location-routes.edit.title"
        title="Load the routes in this page in JOSM"
      />

      @for (route of routes(); track route.id) {
        <ui-list-item [selected]="false">
          <ui-location-route-list-item [routeType]="routeType()" [item]="route" />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [
    EditLinkComponent,
    ListComponent,
    ListItemComponent,
    LocationRoutesFilterComponent,
    LocationRouteListItemComponent,
  ],
})
export class LocationRouteListComponent {
  private readonly service = inject(LocationRoutesPageService);
  private readonly editService = inject(EditService);

  readonly timeInfo = input.required<TimeInfo>();
  readonly routes = input.required<LocationRouteInfo[]>();
  readonly routeCount = input.required<number>();

  protected readonly pageIndex = this.service.pageIndex;
  protected readonly pageSize = this.service.pageSize;
  protected readonly routeType = this.service.routeType;

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePageIndex(pageIndex);
  }

  edit(): void {
    const editParameters: EditParameters = {
      relationIds: this.routes().map((route) => route.id),
      fullRelation: true,
    };
    this.editService.edit(editParameters);
  }
}
