import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { State } from '@app/state/state';
import { MenuItemCheckboxComponent } from './menu-item-checkbox.component';
import { SettingsMenuPoiOptionComponent } from './settings-menu-poi-option.component';

@Component({
  selector: 'ui-settings-menu-poi',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-menu-item-checkbox
      [value]="poiLayerEnabled()"
      (toggle)="togglePoiLayerEnabled()"
      i18n-label="@@poi.menu.pois"
      label="Points of interest"
    />

    <div class="pois">
      <ui-settings-menu-poi-option
        groupName="hiking-biking"
        i18n-label="@@poi.group.hiking-biking"
        label="Hiking/biking"
      />
      <ui-settings-menu-poi-option
        groupName="landmarks"
        i18n-label="@@poi.group.landmarks"
        label="Landmarks"
      />
      <ui-settings-menu-poi-option
        groupName="restaurants"
        i18n-label="@@poi.group.restaurants"
        label="Restaurants"
      />
      <ui-settings-menu-poi-option
        groupName="places-to-stay"
        i18n-label="@@poi.group.places-to-stay"
        label="Places to stay"
      />
      <ui-settings-menu-poi-option
        groupName="tourism"
        i18n-label="@@poi.group.tourism"
        label="Tourism"
      />
      <ui-settings-menu-poi-option
        groupName="amenity"
        i18n-label="@@poi.group.amenity"
        label="Amenity"
      />
      <ui-settings-menu-poi-option groupName="shops" i18n-label="@@poi.group.shops" label="Shops" />
      <ui-settings-menu-poi-option
        groupName="foodshops"
        i18n-label="@@poi.group.foodshops"
        label="Foodshops"
      />
      <ui-settings-menu-poi-option
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
  imports: [SettingsMenuPoiOptionComponent, MenuItemCheckboxComponent],
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
