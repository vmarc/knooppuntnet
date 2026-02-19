import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationPoiInfo } from '@api/common/poi/location-poi-info';
import { PoiLocationPoiListItemComponent } from '@app/poi/internal/list/components/poi-location-poi-list-item.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { PoiLocationPoisPageService } from '../poi-location-pois-page.service';

@Component({
  selector: 'ui-poi-location-poi-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-list
      [pageIndex]="service.pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="service.pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="poiCount()"
    >
      @for (poi of pois(); track poi._id) {
        <ui-list-item [clickable]="true">
          <ui-poi-location-poi-list-item [rowIndex]="poi.rowIndex" [poiInfo]="poi" />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [ListComponent, ListItemComponent, PoiLocationPoiListItemComponent],
})
export class PoiLocationPoiListComponent {
  readonly pois = input.required<ReadonlyArray<LocationPoiInfo>>();
  readonly poiCount = input.required<number>();

  readonly service = inject(PoiLocationPoisPageService);

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePageIndex(pageIndex);
  }
}
