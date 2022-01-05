import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio/radio';
import { LocationRoutesType } from '@api/custom/location-routes-type';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionLocationRoutesType } from '../store/location.actions';
import { selectLocationRoutesPage } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-routes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <div
        *ngIf="locationRoutesPageResponse$ | async as response"
        class="filter"
      >
        <div class="title">Filter</div>

        <mat-radio-group
          [value]="locationRoutesType.all"
          (change)="locationRoutesTypeChanged($event)"
        >
          <mat-radio-button [value]="locationRoutesType.all">
            <span i18n="@@location-routes-sidebar.filter.all">All</span
            ><span class="kpn-brackets">{{
              response.result.allRouteCount
            }}</span>
          </mat-radio-button>
          <mat-radio-button [value]="locationRoutesType.facts">
            <span i18n="@@location-routes-sidebar.filter.facts">Facts</span
            ><span class="kpn-brackets">{{
              response.result.factsRouteCount
            }}</span>
          </mat-radio-button>
          <mat-radio-button [value]="locationRoutesType.inaccessible">
            <span i18n="@@location-routes-sidebar.filter.inaccessible"
              >Inaccessible</span
            ><span class="kpn-brackets">{{
              response.result.inaccessibleRouteCount
            }}</span>
          </mat-radio-button>
          <mat-radio-button [value]="locationRoutesType.survey">
            <span i18n="@@location-routes-sidebar.filter.survey">Survey</span
            ><span class="kpn-brackets">{{
              response.result.surveyRouteCount
            }}</span>
          </mat-radio-button>
        </mat-radio-group>
      </div>
    </kpn-sidebar>
  `,
  styles: [
    `
      .filter {
        padding-top: 25px;
        padding-bottom: 25px;
        padding-left: 25px;
        padding-right: 15px;
      }

      .title {
        padding-bottom: 10px;
      }

      mat-radio-button {
        display: block;
        padding-top: 5px;
        min-height: 17px;
        width: 210px;
      }
    `,
  ],
})
export class LocationRoutesSidebarComponent {
  readonly locationRoutesType = LocationRoutesType;
  readonly locationRoutesPageResponse$ = this.store.select(
    selectLocationRoutesPage
  );

  constructor(private store: Store<AppState>) {}

  locationRoutesTypeChanged(change: MatRadioChange): void {
    const locationRoutesType = change.value as LocationRoutesType;
    this.store.dispatch(actionLocationRoutesType({ locationRoutesType }));
  }
}
