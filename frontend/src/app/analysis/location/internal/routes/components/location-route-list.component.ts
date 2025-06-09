import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TimeInfo } from '@api/common/time-info';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { LocationRoutesFilterComponent } from '@app/analysis/location/internal/routes/components/location-routes-filter.component';
import { DayComponent } from '@app/shared/components/day/day.component';
import { EditService } from '@app/shared/components/edit.service';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';
import { LocationRoutesPageService } from '../location-routes-page.service';
import { LocationRouteAnalysisComponent } from './location-route-analysis';

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
          <div class="kpn-line">
            <span>{{ route.rowIndex + 1 }}</span>
            <ui-action-button-route [routeType]="routeType()" [relationId]="route.id" />
            <ui-link-route
              [routeId]="route.id"
              [routeName]="route.name"
              [routeType]="routeType()"
            />
            <span>{{ (route.meters | integer) + ' m' }}</span>
          </div>
          <div class="kpn-line">
            @if (route.symbol) {
              <ui-symbol [description]="route.symbol" [width]="25" [height]="25" />
            }
            <ui-location-route-analysis [route]="route" [routeType]="routeType()" />

            @if (route.lastSurvey) {
              <span i18n="@@location-routes.table.last-survey" class="kpn-label">Survey</span>
              <span>{{ route.lastSurvey | day }}</span>
            }
            <span>
              <span i18n="@@location-routes.table.last-edit" class="kpn-label">Last edit</span>
              <ui-day [timestamp]="route.lastUpdated" />
            </span>
          </div>
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [
    ActionButtonRouteComponent,
    DayComponent,
    DayPipe,
    EditLinkComponent,
    IntegerFormatPipe,
    LinkRouteComponent,
    ListComponent,
    ListItemComponent,
    LocationRouteAnalysisComponent,
    LocationRoutesFilterComponent,
    SymbolComponent,
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
