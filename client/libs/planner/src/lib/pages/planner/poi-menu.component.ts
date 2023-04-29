import { AsyncPipe } from '@angular/common';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { PoiService } from '@app/services';
import { Store } from '@ngrx/store';
import { actionPlannerPoisEnabled } from '../../store/planner-actions';
import { selectPlannerPoisEnabled } from '../../store/planner-selectors';
import { PoiMenuOptionComponent } from './poi-menu-option.component';

@Component({
  selector: 'kpn-poi-menu',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-divider></mat-divider>

    <mat-checkbox
      (click)="$event.stopPropagation()"
      [checked]="enabled$ | async"
      (change)="enabledChanged($event)"
      class="pois"
      i18n="@@poi.menu.pois"
    >
      Points of interest
    </mat-checkbox>
    <div>
      <kpn-poi-menu-option
        groupName="hiking-biking"
        i18n="@@poi.group.hiking-biking"
      >
        Hiking/biking
      </kpn-poi-menu-option>
      <kpn-poi-menu-option groupName="landmarks" i18n="@@poi.group.landmarks">
        Landmarks
      </kpn-poi-menu-option>
      <kpn-poi-menu-option
        groupName="restaurants"
        i18n="@@poi.group.restaurants"
      >
        Restaurants
      </kpn-poi-menu-option>
      <kpn-poi-menu-option
        groupName="places-to-stay"
        i18n="@@poi.group.places-to-stay"
      >
        Places to stay
      </kpn-poi-menu-option>
      <kpn-poi-menu-option groupName="tourism" i18n="@@poi.group.tourism">
        Tourism
      </kpn-poi-menu-option>
      <kpn-poi-menu-option groupName="amenity" i18n="@@poi.group.amenity">
        Amenity
      </kpn-poi-menu-option>
      <kpn-poi-menu-option groupName="shops" i18n="@@poi.group.shops">
        Shops
      </kpn-poi-menu-option>
      <kpn-poi-menu-option groupName="foodshops" i18n="@@poi.group.foodshops">
        Foodshops
      </kpn-poi-menu-option>
      <kpn-poi-menu-option groupName="sports" i18n="@@poi.group.sports">
        Sports
      </kpn-poi-menu-option>
    </div>
  `,
  styles: [
    `
      mat-divider {
        margin-top: 5px;
        margin-bottom: 5px;
      }

      .pois {
        padding-right: 20px;
      }
    `,
  ],
  standalone: true,
  imports: [
    MatDividerModule,
    MatCheckboxModule,
    PoiMenuOptionComponent,
    AsyncPipe,
  ],
})
export class PoiMenuComponent {
  readonly enabled$ = this.store.select(selectPlannerPoisEnabled);

  constructor(private store: Store, private service: PoiService) {}

  enabledChanged(event: MatCheckboxChange): void {
    this.store.dispatch(actionPlannerPoisEnabled({ enabled: event.checked }));
    // this.service.updateEnabled(event.checked);
  }
}