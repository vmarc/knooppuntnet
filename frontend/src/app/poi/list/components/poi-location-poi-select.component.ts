import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { NzFormControlComponent } from 'ng-zorro-antd/form';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzColDirective } from 'ng-zorro-antd/grid';
import { NzRowDirective } from 'ng-zorro-antd/grid';
import { LocationSelectorComponent } from '../../../analysis/location/location-selector.component';
import { PoiLocationPoisPageService } from '../poi-location-pois-page.service';
import { CountrySelectComponent } from './country-select.component';

@Component({
  selector: 'kpn-location-poi-select',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="poi-select-form">
      <nz-form-item>
        <nz-form-control>
          <kpn-country-select />
        </nz-form-control>
      </nz-form-item>

      @if (service.locationNode(); as locationNode) {
        <kpn-location-selector
          [country]="service.country()"
          [locationNode]="locationNode"
          [all]="true"
          (selection)="locationSelectionChanged($event)"
        />
      }

      @if (service.summaryResponse(); as response) {
        <nz-form-item>
          <nz-form-control>
            <button nz-button (click)="listPois()">List pois</button>
          </nz-form-control>
        </nz-form-item>

        @if (response.result; as page) {
          @for (group of page.groups; track group) {
            <div>
              <label nz-checkbox> {{ group.name }} </label>
            </div>
            <div class="poi-group-body">
              @for (poiCount of group.poiCounts; track poiCount) {
                @if (poiCount.count > 0) {
                  <div>
                    <label
                      nz-checkbox
                      (nzCheckedChange)="poiSelectionChanged(poiCount.name, $event)"
                    >
                      <span class="poi-name">{{ poiCount.name }}</span>
                      <span class="poi-count">{{ poiCount.count }}</span>
                    </label>
                  </div>
                }
              }
            </div>
          }
        }
      }
    </div>
  `,
  styles: `
    .poi-select-form {
      max-width: 20em;
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
  imports: [
    CountrySelectComponent,
    LocationSelectorComponent,
    MatCheckboxModule,
    NzButtonComponent,
    NzCheckboxComponent,
    NzColDirective,
    NzFormControlComponent,
    NzFormItemComponent,
    NzRowDirective,
    ReactiveFormsModule,
  ],
})
export class LocationPoiSelectComponent {
  readonly service = inject(PoiLocationPoisPageService);

  locationSelectionChanged(location: string): void {
    this.service.updateLocation(location);
  }

  listPois(): void {
    this.service.listPois();
  }

  poiSelectionChanged(poiName: string, checked: boolean): void {
    if (checked) {
      this.service.updateLayers(poiName);
    } else {
      this.service.updateLayers('');
    }
  }
}
