import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { LocationNodesType } from '@api/custom/location-nodes-type';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionLocationNodesType } from '../store/location.actions';
import { selectLocationNodesPage } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <div
        *ngIf="locationNodesPageResponse$ | async as response"
        class="filter"
      >
        <div class="title">Filter</div>

        <mat-radio-group
          [value]="locationNodesType.all"
          (change)="locationNodesTypeChanged($event)"
        >
          <mat-radio-button [value]="locationNodesType.all">
            <span i18n="@@location-nodes-sidebar.filter.all">All</span
            ><span class="kpn-brackets">{{
              response.result.allNodeCount
            }}</span>
          </mat-radio-button>
          <mat-radio-button [value]="locationNodesType.facts">
            <span i18n="@@location-nodes-sidebar.filter.facts">Facts</span
            ><span class="kpn-brackets">{{
              response.result.factsNodeCount
            }}</span>
          </mat-radio-button>
          <mat-radio-button [value]="locationNodesType.survey">
            <span i18n="@@location-nodes-sidebar.filter.survey">Survey</span
            ><span class="kpn-brackets">{{
              response.result.surveyNodeCount
            }}</span>
          </mat-radio-button>
          <mat-radio-button [value]="locationNodesType.integrityCheckFailed">
            <span i18n="@@location-nodes-sidebar.filter.integrity-check-failed"
              >Unexpected route count</span
            ><span class="kpn-brackets">{{
              response.result.integrityCheckFailedNodeCount
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
export class LocationNodesSidebarComponent {
  readonly locationNodesType = LocationNodesType;
  readonly locationNodesPageResponse$ = this.store.select(
    selectLocationNodesPage
  );

  constructor(private store: Store<AppState>) {}

  locationNodesTypeChanged(change: MatRadioChange): void {
    const locationNodesType = change.value as LocationNodesType;
    this.store.dispatch(actionLocationNodesType({ locationNodesType }));
  }
}
