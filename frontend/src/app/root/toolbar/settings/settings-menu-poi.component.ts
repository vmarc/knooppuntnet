import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { ChangeDetectionStrategy } from '@angular/core';
import { State } from '@app/state';
import { SettingsMenuPoiOptionComponent } from './settings-menu-poi-option.component';

@Component({
  selector: 'kpn-settings-menu-poi',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-checkbox
      (click)="$event.stopPropagation()"
      [checked]="poiLayerEnabled()"
      (change)="poiLayerEnabledChanged($event)"
      class="pois"
      i18n="@@poi.menu.pois"
    >
      Points of interest
    </mat-checkbox>
    <div>
      <kpn-settings-menu-poi-option groupName="hiking-biking" i18n="@@poi.group.hiking-biking">
        Hiking/biking
      </kpn-settings-menu-poi-option>
      <kpn-settings-menu-poi-option groupName="landmarks" i18n="@@poi.group.landmarks">
        Landmarks
      </kpn-settings-menu-poi-option>
      <kpn-settings-menu-poi-option groupName="restaurants" i18n="@@poi.group.restaurants">
        Restaurants
      </kpn-settings-menu-poi-option>
      <kpn-settings-menu-poi-option groupName="places-to-stay" i18n="@@poi.group.places-to-stay">
        Places to stay
      </kpn-settings-menu-poi-option>
      <kpn-settings-menu-poi-option groupName="tourism" i18n="@@poi.group.tourism">
        Tourism
      </kpn-settings-menu-poi-option>
      <kpn-settings-menu-poi-option groupName="amenity" i18n="@@poi.group.amenity">
        Amenity
      </kpn-settings-menu-poi-option>
      <kpn-settings-menu-poi-option groupName="shops" i18n="@@poi.group.shops">
        Shops
      </kpn-settings-menu-poi-option>
      <kpn-settings-menu-poi-option groupName="foodshops" i18n="@@poi.group.foodshops">
        Foodshops
      </kpn-settings-menu-poi-option>
      <kpn-settings-menu-poi-option groupName="sports" i18n="@@poi.group.sports">
        Sports
      </kpn-settings-menu-poi-option>
    </div>
  `,
  styles: `
    .pois {
      padding-right: 20px;
    }
  `,
  imports: [MatCheckboxModule, SettingsMenuPoiOptionComponent],
})
export class SettingsMenuPoiComponent {
  private readonly state = inject(State);
  private readonly layers = this.state.map.layers;
  readonly poiLayerEnabled = this.layers.poiLayerEnabled;

  poiLayerEnabledChanged(event: MatCheckboxChange): void {
    this.layers.updatePoiLayerEnabled(event.checked);
  }
}
