import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { NzTabSetComponent } from 'ng-zorro-antd/tabs';
import { NzTabComponent } from 'ng-zorro-antd/tabs';
import { PageComponent } from '../../shared/components/page/page.component';
import { RouterService } from '../../shared/services/router.service';
import { LocationPoiSelectComponent } from './components/poi-location-poi-select.component';
import { PoiLocationPoiTableComponent } from './components/poi-location-poi-table.component';
import { PoiLocationPoisPageService } from './poi-location-pois-page.service';

@Component({
  selector: 'kpn-poi-location-pois-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <nz-tabset [nzSelectedIndex]="selectedTabIndex()">
        <nz-tab nzTitle="Select">
          <kpn-location-poi-select />
        </nz-tab>
        <nz-tab nzTitle="Pois">
          @if (poisResponse(); as response) {
            @if (response.result; as page) {
              <kpn-poi-location-poi-table [pois]="page.pois" [poiCount]="page.poiCount" />
            }
          } @else {
            First select location and poi type to see poi list.
          }
        </nz-tab>
      </nz-tabset>
    </kpn-page>
  `,
  providers: [PoiLocationPoisPageService, RouterService],
  imports: [
    LocationPoiSelectComponent,
    NzTabComponent,
    NzTabSetComponent,
    PageComponent,
    PoiLocationPoiTableComponent,
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
