import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { LocationSelectorComponent } from '@app/analysis/location';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { PoiLocationPoisPageService } from '../poi-location-pois-page.service';
import { CountrySelectComponent } from './country-select.component';

@Component({
  selector: 'kpn-location-pois-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <div class="location-selector">
        <kpn-country-select />

        @if (service.locationNode(); as locationNode) {
          <div>
            <kpn-location-selector
              [country]="service.country()"
              [locationNode]="locationNode"
              [all]="true"
              (selection)="locationSelectionChanged($event)"
            />
          </div>
        }
      </div>

      @if (service.summaryResponse(); as response) {
        <button mat-stroked-button (click)="listPois()">List pois</button>

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
                    <mat-checkbox (change)="poiSelectionChanged(poiCount.name, $event)">
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
    MatButton,
  ],
})
export class LocationPoisSidebarComponent {
  protected readonly service = inject(PoiLocationPoisPageService);

  locationSelectionChanged(location: string): void {
    this.service.setLocation(location);
  }

  listPois(): void {
    this.service.listPois();
  }

  poiSelectionChanged(poiName: string, event: MatCheckboxChange): void {
    if (event.checked) {
      this.service.setLayers(poiName);
    } else {
      this.service.setLayers('');
    }
  }
}
