import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkFactsPageDestroy } from '../store/network.actions';
import { actionNetworkFactsPageInit } from '../store/network.actions';
import { selectNetworkFactsPage } from '../store/network.selectors';
import { NetworkFactsComponent } from './components/network-facts.component';

@Component({
  selector: 'kpn-network-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-network-page-header
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@network-facts.title"
      />
      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@network-page.network-not-found">Network not found</p>
          } @else {
            <kpn-network-facts [apiResponse]="response" />
          }
        </div>
      }
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    NetworkPageHeaderComponent,
    PageComponent,
    NetworkFactsComponent,
  ],
})
export class NetworkFactsPageComponent implements OnInit, OnDestroy {
  private readonly store = inject(Store);
  protected readonly apiResponse = this.store.selectSignal(selectNetworkFactsPage);

  ngOnInit(): void {
    this.store.dispatch(actionNetworkFactsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNetworkFactsPageDestroy());
  }
}
