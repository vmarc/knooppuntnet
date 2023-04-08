import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionSubsetMapPageInit } from '../store/subset.actions';
import { selectSubsetMapPage } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@subset-map.title"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response">
      <kpn-subset-map />
    </div>
  `,
})
export class SubsetMapPageComponent implements OnInit {
  readonly response$ = this.store.select(selectSubsetMapPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetMapPageInit());
  }
}
