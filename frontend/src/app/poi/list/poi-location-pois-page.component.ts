import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { OldPageComponent } from '@app/components/shared/page';
import { RouterService } from '../../shared/services/router.service';
import { PoiLocationPoiTableComponent } from './components/poi-location-poi-table.component';
import { LocationPoisSidebarComponent } from './components/poi-location-pois-sidebar.component';
import { PoiLocationPoisPageService } from './poi-location-pois-page.service';

@Component({
  selector: 'kpn-poi-location-pois-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      @if (service.poisResponse(); as response) {
        @if (response.result; as page) {
          <kpn-poi-location-poi-table [pois]="page.pois" [poiCount]="page.poiCount" />
        }
      }
      <kpn-location-pois-sidebar sidebar />
    </kpn-old-page>
  `,
  providers: [PoiLocationPoisPageService, RouterService],
  imports: [LocationPoisSidebarComponent, OldPageComponent, PoiLocationPoiTableComponent],
})
export class PoiLocationPoisPageComponent implements OnInit {
  readonly service = inject(PoiLocationPoisPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
