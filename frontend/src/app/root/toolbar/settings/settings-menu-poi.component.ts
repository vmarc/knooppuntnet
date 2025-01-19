import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { ChangeDetectionStrategy } from '@angular/core';
import { State } from '@app/state';
import { MenuItemCheckboxComponent } from './menu-item-checkbox.component';
import { SettingsMenuPoiOptionComponent } from './settings-menu-poi-option.component';

@Component({
  selector: 'kpn-settings-menu-poi',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-menu-item-checkbox
      [value]="poiLayerEnabled()"
      (toggle)="togglePoiLayerEnabled()"
      i18n-label="@@poi.menu.pois"
      label="Points of interest"
    />

    <div class="pois">
      <kpn-settings-menu-poi-option
        groupName="hiking-biking"
        i18n-label="@@poi.group.hiking-biking"
        label="Hiking/biking"
      />
      <kpn-settings-menu-poi-option
        groupName="landmarks"
        i18n-label="@@poi.group.landmarks"
        label="Landmarks"
      />
      <kpn-settings-menu-poi-option
        groupName="restaurants"
        i18n-label="@@poi.group.restaurants"
        label="Restaurants"
      />
      <kpn-settings-menu-poi-option
        groupName="places-to-stay"
        i18n-label="@@poi.group.places-to-stay"
        label="Places to stay"
      />
      <kpn-settings-menu-poi-option
        groupName="tourism"
        i18n-label="@@poi.group.tourism"
        label="Tourism"
      />
      <kpn-settings-menu-poi-option
        groupName="amenity"
        i18n-label="@@poi.group.amenity"
        label="Amenity"
      />
      <kpn-settings-menu-poi-option
        groupName="shops"
        i18n-label="@@poi.group.shops"
        label="Shops"
      />
      <kpn-settings-menu-poi-option
        groupName="foodshops"
        i18n-label="@@poi.group.foodshops"
        label="Foodshops"
      />
      <kpn-settings-menu-poi-option
        groupName="sports"
        i18n-label="@@poi.group.sports"
        label="Sports"
      />
    </div>
  `,
  styles: `
    .pois {
      padding-left: 20px;
      padding-right: 20px;
    }
  `,
  imports: [MatCheckboxModule, SettingsMenuPoiOptionComponent, MenuItemCheckboxComponent],
})
export class SettingsMenuPoiComponent {
  private readonly state = inject(State);
  private readonly layers = this.state.map.layers;
  readonly poiLayerEnabled = this.layers.poiLayerEnabled;

  togglePoiLayerEnabled(): void {
    const value = this.poiLayerEnabled();
    this.layers.updatePoiLayerEnabled(!value);
  }
}
