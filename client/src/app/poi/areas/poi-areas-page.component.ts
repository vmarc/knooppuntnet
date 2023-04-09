import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { selectPoiAreasPage } from '@app/poi/store/poi.selectors';
import { Store } from '@ngrx/store';
import { actionPoiAreasPageInit } from '@app/poi/store/poi.actions';

@Component({
  selector: 'kpn-poi-areas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header>
      <span i18n="@@poi-areas.title">Point of interest areas</span>
    </kpn-page-header>
    <p i18n="@@poi-areas.comment">
      This map shows the bounding boxes that are used to determine where to
      collect the point of interest information.
    </p>

    <div *ngIf="response$ | async as response">
      <kpn-poi-map />
    </div>
  `,
})
export class PoiAreasPageComponent implements OnInit {
  protected readonly response$ = this.store.select(selectPoiAreasPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionPoiAreasPageInit());
  }
}
