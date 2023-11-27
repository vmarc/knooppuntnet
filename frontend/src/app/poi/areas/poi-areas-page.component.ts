import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { BaseSidebarComponent } from '@app/shared/base';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { actionPoiAreasPageInit } from '../store/poi.actions';
import { selectPoiAreasPage } from '../store/poi.selectors';
import { PoiMapComponent } from './poi-map.component';

@Component({
  selector: 'kpn-poi-areas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-page-header>
        <span i18n="@@poi-areas.title">Point of interest areas</span>
      </kpn-page-header>
      <p i18n="@@poi-areas.comment">
        This map shows the bounding boxes that are used to determine where to collect the point of
        interest information.
      </p>

      <div *ngIf="apiResponse() as response">
        <kpn-poi-map />
      </div>
      <kpn-base-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    BaseSidebarComponent,
    NgIf,
    PageComponent,
    PageHeaderComponent,
    PoiMapComponent,
  ],
})
export class PoiAreasPageComponent implements OnInit {
  readonly apiResponse = this.store.selectSignal(selectPoiAreasPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionPoiAreasPageInit());
  }
}
