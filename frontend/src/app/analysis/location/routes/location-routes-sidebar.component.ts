import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { LocationRoutesType } from '@api/custom';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { Store } from '@ngrx/store';
import { actionLocationRoutesType } from '../store/location.actions';
import { selectLocationRoutesPage } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-routes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      @if (apiResponse(); as response) {
        <div class="filter">
          <div class="title" i18n="@@location-routes-sidebar.filter.title">Filter</div>
          <mat-radio-group
            [value]="locationRoutesType.all"
            (change)="locationRoutesTypeChanged($event)"
          >
            <div>
              <mat-radio-button [value]="locationRoutesType.all">
                <span i18n="@@location-routes-sidebar.filter.all">All</span
                ><span class="kpn-brackets">{{ response.result.allRouteCount }}</span>
              </mat-radio-button>
            </div>
            <div>
              <mat-radio-button [value]="locationRoutesType.facts">
                <span i18n="@@location-routes-sidebar.filter.facts">Facts</span
                ><span class="kpn-brackets">{{ response.result.factsRouteCount }}</span>
              </mat-radio-button>
            </div>
            <div>
              <mat-radio-button [value]="locationRoutesType.inaccessible">
                <span i18n="@@location-routes-sidebar.filter.inaccessible">Inaccessible</span
                ><span class="kpn-brackets">{{ response.result.inaccessibleRouteCount }}</span>
              </mat-radio-button>
            </div>
            <div>
              <mat-radio-button [value]="locationRoutesType.survey">
                <span i18n="@@location-routes-sidebar.filter.survey">Survey</span
                ><span class="kpn-brackets">{{ response.result.surveyRouteCount }}</span>
              </mat-radio-button>
            </div>
          </mat-radio-group>
        </div>
      }
    </kpn-sidebar>
  `,
  styles: `
    .filter {
      padding: 25px 15px 25px 25px;
    }

    .title {
      padding-bottom: 10px;
    }
  `,
  standalone: true,
  imports: [SidebarComponent, MatRadioModule, AsyncPipe],
})
export class LocationRoutesSidebarComponent {
  readonly locationRoutesType = LocationRoutesType;
  readonly apiResponse = this.store.selectSignal(selectLocationRoutesPage);

  constructor(private store: Store) {}

  locationRoutesTypeChanged(change: MatRadioChange): void {
    const locationRoutesType = change.value as LocationRoutesType;
    this.store.dispatch(actionLocationRoutesType({ locationRoutesType }));
  }
}
