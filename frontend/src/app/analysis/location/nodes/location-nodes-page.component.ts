import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationNodesSidebarComponent } from './components/location-nodes-sidebar.component';
import { LocationNodesComponent } from './components/location-nodes.component';
import { LocationNodesPageService } from './location-nodes-page.service';

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
      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <kpn-location-response [response]="response">
            <kpn-location-nodes [page]="response.result" />
          </kpn-location-response>
        </div>
      }
      <kpn-location-nodes-sidebar sidebar />
    </kpn-page>
  `,
  providers: [LocationNodesPageService, RouterService],
  standalone: true,
  imports: [
    ErrorComponent,
    LocationNodesComponent,
    LocationNodesSidebarComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    PageComponent,
  ],
})
export class LocationNodesPageComponent implements OnInit {
  protected readonly service = inject(LocationNodesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
