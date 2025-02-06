import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SidebarFooterComponent } from '@app/shared/components/sidebar/sidebar-footer.component';
import { RouterService } from '../../../../shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationFactsComponent } from './components/location-facts.component';
import { LocationFactsPageService } from './location-facts-page.service';

@Component({
  selector: 'kpn-location-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-location-page-header
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@location-facts.title"
      />

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <kpn-location-response [response]="response">
            <kpn-location-facts [locationFacts]="response.result.locationFacts" />
          </kpn-location-response>
        </div>
      }
    </kpn-page>
    <kpn-sidebar-footer />
  `,
  providers: [LocationFactsPageService, RouterService],
  imports: [
    ErrorComponent,
    LocationFactsComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    PageComponent,
    SidebarFooterComponent,
  ],
})
export class LocationFactsPageComponent implements OnInit {
  protected readonly service = inject(LocationFactsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
