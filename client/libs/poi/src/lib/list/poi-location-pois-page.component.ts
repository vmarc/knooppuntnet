import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionLocationPoisPageInit } from '../store/poi.actions';
import { selectLocationPoisPage } from '../store/poi.selectors';

@Component({
  selector: 'kpn-poi-location-pois-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="response$ | async as response">
      <div *ngIf="response.result as page">
        <kpn-poi-location-poi-table
          [pois]="page.pois"
          [poiCount]="page.poiCount"
        />
      </div>
    </div>
  `,
})
export class PoiLocationPoisPageComponent implements OnInit {
  readonly response$ = this.store.select(selectLocationPoisPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationPoisPageInit());
  }
}
