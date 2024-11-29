import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { DividerComponent } from '@app/components/shared';
import { ChangeDetectionStrategy } from '@angular/core';
import { State } from '@app/state';
import { ExplorePoiOptionComponent } from './explore-poi-option.component';

@Component({
  selector: 'kpn-explore-poi',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-divider />

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
      <kpn-explore-poi-option groupName="hiking-biking" i18n="@@poi.group.hiking-biking">
        Hiking/biking
      </kpn-explore-poi-option>
      <kpn-explore-poi-option groupName="landmarks" i18n="@@poi.group.landmarks">
        Landmarks
      </kpn-explore-poi-option>
      <kpn-explore-poi-option groupName="restaurants" i18n="@@poi.group.restaurants">
        Restaurants
      </kpn-explore-poi-option>
      <kpn-explore-poi-option groupName="places-to-stay" i18n="@@poi.group.places-to-stay">
        Places to stay
      </kpn-explore-poi-option>
      <kpn-explore-poi-option groupName="tourism" i18n="@@poi.group.tourism">
        Tourism
      </kpn-explore-poi-option>
      <kpn-explore-poi-option groupName="amenity" i18n="@@poi.group.amenity">
        Amenity
      </kpn-explore-poi-option>
      <kpn-explore-poi-option groupName="shops" i18n="@@poi.group.shops"
        >Shops
      </kpn-explore-poi-option>
      <kpn-explore-poi-option groupName="foodshops" i18n="@@poi.group.foodshops">
        Foodshops
      </kpn-explore-poi-option>
      <kpn-explore-poi-option groupName="sports" i18n="@@poi.group.sports">
        Sports
      </kpn-explore-poi-option>
    </div>
  `,
  styles: `
    .pois {
      padding-right: 20px;
    }
  `,
  standalone: true,
  imports: [MatCheckboxModule, DividerComponent, ExplorePoiOptionComponent],
})
export class ExplorePoiComponent {
  private readonly state = inject(State);
  protected readonly poiLayerEnabled = this.state.map.poiLayerEnabled;

  poiLayerEnabledChanged(event: MatCheckboxChange): void {
    this.state.map.updatePoiLayerEnabled(event.checked);
  }
}
