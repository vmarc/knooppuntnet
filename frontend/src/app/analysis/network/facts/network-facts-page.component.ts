import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkFactsComponent } from './components/network-facts.component';
import { NetworkFactsStore } from './network-facts.store';

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
      @if (store.response(); as response) {
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
  providers: [NetworkFactsStore, RouterService],
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    NetworkPageHeaderComponent,
    PageComponent,
    NetworkFactsComponent,
  ],
})
export class NetworkFactsPageComponent {
  protected readonly store = inject(NetworkFactsStore);
}
