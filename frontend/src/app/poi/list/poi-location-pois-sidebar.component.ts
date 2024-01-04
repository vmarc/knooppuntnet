import { AsyncPipe } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Country } from '@api/custom';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { Store } from '@ngrx/store';
import { LocationSelectorComponent } from '@app/analysis/location';
import { actionLocationPoiSummaryCountryChanged } from '../store/poi.actions';
import { actionLocationPoiSummaryPageInit } from '../store/poi.actions';
import { selectLocationPoiSummaryLocationNode } from '../store/poi.selectors';
import { selectLocationPoiSummaryPage } from '../store/poi.selectors';
import { CountrySelectComponent } from './country-select.component';

@Component({
  selector: 'kpn-location-pois-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <div class="location-selector">
        <kpn-country-select (country)="countryChanged($event)" />

        @if (locationNode(); as locationNode) {
          <div>
            <kpn-location-selector
              [country]="country"
              [locationNode]="locationNode"
              [all]="true"
              (selection)="locationSelectionChanged($event)"
            />
          </div>
        }
      </div>

      @if (apiResponse(); as response) {
        <div class="filter">
          @if (response.result; as page) {
            @for (group of page.groups; track group) {
              <div>
                <mat-checkbox>
                  <span class="poi-group-name">{{ group.name }}</span>
                </mat-checkbox>
              </div>
              <div class="poi-group-body">
                @for (poiCount of group.poiCounts; track poiCount) {
                  <div>
                    <mat-checkbox>
                      <span class="poi-name">{{ poiCount.name }}</span>
                      <span class="poi-count">{{ poiCount.count }}</span>
                    </mat-checkbox>
                  </div>
                }
              </div>
            }
          }
        </div>
      }
    </kpn-sidebar>
  `,
  styles: `
    .location-selector {
      padding: 15px;
    }

    .filter {
      padding: 25px 15px 25px 25px;
    }

    .poi-group-name {
      display: inline-block;
      font-weight: bold;
    }

    .poi-group-body {
      padding-top: 5px;
      padding-left: 25px;
      padding-bottom: 20px;
    }

    .poi-name {
      display: inline-block;
      width: 160px;
      max-width: 160px;
      word-wrap: break-word;
    }

    .poi-count {
      display: inline-flex;
      width: 50px;
      justify-content: right;
    }
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    CountrySelectComponent,
    LocationSelectorComponent,
    MatCheckboxModule,
    SidebarComponent,
  ],
})
export class LocationPoisSidebarComponent implements OnInit {
  readonly apiResponse = this.store.selectSignal(selectLocationPoiSummaryPage);
  readonly locationNode = this.store.selectSignal(selectLocationPoiSummaryLocationNode);

  country = Country.be;

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationPoiSummaryPageInit());
  }

  countryChanged(country: Country): void {
    console.log('country changed: ' + country);
    if (country) {
      this.store.dispatch(actionLocationPoiSummaryCountryChanged({ country }));
    }
  }

  locationSelectionChanged(location: string): void {
    console.log('locationSelectionChanged: ' + location);
  }
}
