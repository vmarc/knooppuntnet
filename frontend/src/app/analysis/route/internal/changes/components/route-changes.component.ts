import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter/change-filter.component';
import { RouteChangeComponent } from '@app/analysis/route/internal/changes/components/route-change.component';
import { ChangeOption } from '@app/shared/kpn/common/change-option';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { RouteChangesPageService } from '../route-changes-page.service';

@Component({
  selector: 'ui-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p>
        <ui-situation-on [timestamp]="service.response().situationOn" />
      </p>
      <ui-change-filter
        [filterOptions]="service.filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />
      <ui-changes
        [impact]="service.impact()"
        [pageSize]="service.pageSize()"
        [pageIndex]="service.pageIndex()"
        (impactChange)="onImpactChange($event)"
        (pageSizeChange)="onPageSizeChange($event)"
        (pageIndexChange)="onPageIndexChange($event)"
        [totalCount]="service.response().result.totalCount"
        [changeCount]="service.response().result.changeCount"
      >
        <ui-items>
          @for (routeChangeInfo of service.response().result.changes; track routeChangeInfo) {
            <ui-item [index]="routeChangeInfo.rowIndex">
              <ui-route-change [routeChangeInfo]="routeChangeInfo" />
            </ui-item>
          }
        </ui-items>
      </ui-changes>
    </div>
  `,
  providers: [RouterService],
  imports: [
    ChangeFilterComponent,
    ChangesComponent,
    ItemComponent,
    ItemsComponent,
    RouteChangeComponent,
    SituationOnComponent,
  ],
})
export class RouteChangesComponent {
  protected readonly service = inject(RouteChangesPageService);

  onImpactChange(impact: boolean): void {
    this.service.updateImpact(impact);
  }

  onPageSizeChange(pageSize: number): void {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number): void {
    this.service.updatePageIndex(pageIndex);
  }

  onOptionSelected(option: ChangeOption): void {
    this.service.updateFilterOption(option);
  }
}
