import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { actionLocationNodesPageDestroy } from '../store/location.actions';
import { actionLocationNodesPageInit } from '../store/location.actions';
import { selectLocationNodesPage } from '../store/location.selectors';
import { LocationNodesSidebarComponent } from './location-nodes-sidebar.component';
import { LocationNodesComponent } from './location-nodes.component';

@Component({
  selector: 'kpn-location-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-location-page-header
        pageName="nodes"
        pageTitle="Nodes"
        i18n-pageTitle="@@location-nodes.title"
      />

      <kpn-error />
      <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
        <kpn-location-response [response]="response">
          <kpn-location-nodes [page]="response.result" />
        </kpn-location-response>
      </div>
      <kpn-location-nodes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    LocationNodesComponent,
    LocationNodesSidebarComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    NgIf,
    PageComponent,
  ],
})
export class LocationNodesPageComponent implements OnInit, OnDestroy {
  readonly apiResponse = this.store.selectSignal(selectLocationNodesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationNodesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationNodesPageDestroy());
  }
}
