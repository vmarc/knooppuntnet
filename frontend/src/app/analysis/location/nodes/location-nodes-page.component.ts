import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { SidebarFooterComponent } from '@app/components/shared/sidebar';
import { PageFilterComponent } from '../../../shared/components/shared/page/page-filter.component';
import { RouterService } from '../../../shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationNodesFilterComponent } from './components/location-nodes-filter.component';
import { LocationNodesComponent } from './components/location-nodes.component';
import { LocationNodesPageService } from './location-nodes-page.service';

@Component({
  selector: 'kpn-location-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-filter>
      <kpn-location-page-header
        pageName="nodes"
        pageTitle="Nodes"
        i18n-pageTitle="@@location-nodes.title"
      />

      <kpn-error />
      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <kpn-location-response [response]="response">
            <kpn-location-nodes [page]="response.result" />
          </kpn-location-response>
        </div>
      }
      <kpn-location-nodes-filter filter />
    </kpn-page-filter>
    <kpn-sidebar-footer />
  `,
  providers: [LocationNodesPageService, RouterService],
  standalone: true,
  imports: [
    ErrorComponent,
    LocationNodesComponent,
    LocationNodesFilterComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    SidebarFooterComponent,
    PageFilterComponent,
  ],
})
export class LocationNodesPageComponent implements OnInit {
  protected readonly service = inject(LocationNodesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
