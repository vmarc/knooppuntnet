import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetMapPageInit } from '../store/subset.actions';
import { selectSubsetMapPage } from '../store/subset.selectors';
import { SubsetSidebarComponent } from '../subset-sidebar.component';
import { SubsetMapComponent } from './subset-map.component';

@Component({
  selector: 'kpn-subset-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="map"
        pageTitle="Map"
        i18n-pageTitle="@@subset-map.title"
      />

      <kpn-error />

      @if (apiResponse(); as response) {
        <kpn-subset-map />
      }
      <kpn-subset-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    PageComponent,
    SubsetMapComponent,
    SubsetPageHeaderBlockComponent,
    SubsetSidebarComponent,
  ],
})
export class SubsetMapPageComponent implements OnInit {
  readonly apiResponse = this.store.selectSignal(selectSubsetMapPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetMapPageInit());
  }
}
