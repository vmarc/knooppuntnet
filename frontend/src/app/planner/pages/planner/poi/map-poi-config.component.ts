import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiGroupSportsComponent } from './poi-group-sports.component';
import { PoiGroupFoodshopsComponent } from './poi-group-foodshops.component';
import { PoiGroupShopsComponent } from './poi-group-shops.component';
import { PoiGroupAmenityComponent } from './poi-group-amenity.component';
import { PoiGroupTourismComponent } from './poi-group-tourism.component';
import { PoiGroupPlacesToStayComponent } from './poi-group-places-to-stay.component';
import { PoiGroupRestaurantsComponent } from './poi-group-restaurants.component';
import { PoiGroupLandmarksComponent } from './poi-group-landmarks.component';
import { PoiGroupHikingBikingComponent } from './poi-group-hiking-biking.component';

@Component({
  selector: 'kpn-map-poi-config',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group-hiking-biking />
    <kpn-poi-group-landmarks />
    <kpn-poi-group-restaurants />
    <kpn-poi-group-places-to-stay />
    <kpn-poi-group-tourism />
    <kpn-poi-group-amenity />
    <kpn-poi-group-shops />
    <kpn-poi-group-foodshops />
    <kpn-poi-group-sports />
  `,
  styles: `
    ::ng-deep .subset-title {
      margin-top: 40px;
      margin-bottom: 20px;
      font-weight: 600;
    }

    ::ng-deep .col-icon {
      display: inline-block;
      position: relative;
      width: 35px;
    }

    ::ng-deep .col-spacer {
      display: inline-block;
      position: relative;
      width: 40px;
    }

    ::ng-deep .col-name {
      display: inline-block;
      position: relative;
      width: 130px;
      line-height: 37px;
      vertical-align: top;
    }

    ::ng-deep .col-level-0 {
      display: inline-block;
      position: relative;
      width: 40px;
      vertical-align: top;
    }

    ::ng-deep .col-level-11 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

    ::ng-deep .col-level-12 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

    ::ng-deep .col-level-13 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

    ::ng-deep .col-level-14 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

    ::ng-deep .col-level-15 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }
  `,
  standalone: true,
  imports: [
    PoiGroupAmenityComponent,
    PoiGroupFoodshopsComponent,
    PoiGroupHikingBikingComponent,
    PoiGroupLandmarksComponent,
    PoiGroupPlacesToStayComponent,
    PoiGroupRestaurantsComponent,
    PoiGroupShopsComponent,
    PoiGroupSportsComponent,
    PoiGroupTourismComponent,
  ],
})
export class MapPoiConfigComponent {}
