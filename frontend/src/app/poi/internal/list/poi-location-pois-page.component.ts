import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { NzTabsComponent } from 'ng-zorro-antd/tabs';
import { NzTabComponent } from 'ng-zorro-antd/tabs';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { LocationPoiSelectComponent } from './components/poi-location-poi-select.component';
import { PoiLocationPoiListComponent } from './components/poi-location-poi-list.component';
import { PoiLocationPoisPageService } from './poi-location-pois-page.service';

@Component({
  selector: 'ui-poi-location-pois-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <nz-tabset [nzSelectedIndex]="selectedTabIndex()">
        <nz-tab nzTitle="Select">
          <ui-location-poi-select />
        </nz-tab>
        <nz-tab nzTitle="Pois">
          @if (poisResponse(); as response) {
            @if (response.result; as page) {
              <ui-poi-location-poi-list [pois]="page.pois" [poiCount]="page.poiCount" />
            }
          } @else {
            First select location and poi type to see poi list.
          }
        </nz-tab>
      </nz-tabset>
    </ui-page>
  `,
  providers: [PoiLocationPoisPageService, RouterService],
  imports: [
    LocationPoiSelectComponent,
    NzTabComponent,
    PageComponent,
    PoiLocationPoiListComponent,
    NzTabsComponent,
  ],
})
export class PoiLocationPoisPageComponent implements OnInit {
  private readonly service = inject(PoiLocationPoisPageService);
  readonly selectedTabIndex = this.service.selectedTabIndex;
  readonly poisResponse = this.service.poisResponse;

  ngOnInit(): void {
    this.service.onInit();
  }
}
