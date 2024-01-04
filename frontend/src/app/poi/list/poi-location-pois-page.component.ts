import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { actionLocationPoisPageInit } from '../store/poi.actions';
import { selectLocationPoisPage } from '../store/poi.selectors';
import { PoiLocationPoiTableComponent } from './poi-location-poi-table.component';
import { LocationPoisSidebarComponent } from './poi-location-pois-sidebar.component';

@Component({
  selector: 'kpn-poi-location-pois-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      @if (apiResponse(); as response) {
        @if (response.result; as page) {
          <kpn-poi-location-poi-table [pois]="page.pois" [poiCount]="page.poiCount" />
        }
      }
      <kpn-location-pois-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [AsyncPipe, LocationPoisSidebarComponent, PageComponent, PoiLocationPoiTableComponent],
})
export class PoiLocationPoisPageComponent implements OnInit {
  readonly apiResponse = this.store.selectSignal(selectLocationPoisPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationPoisPageInit());
  }
}
