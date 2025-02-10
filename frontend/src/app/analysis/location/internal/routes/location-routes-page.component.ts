import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationRoutesFilterComponent } from './components/location-routes-filter.component';
import { LocationRoutesComponent } from './components/location-routes.component';
import { LocationRoutesPageService } from './location-routes-page.service';

@Component({
  selector: 'kpn-location-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <div class="page">
        <div class="page-left">
          <div>
            <kpn-location-routes-filter />
          </div>
        </div>
        <div class="page-right">
          <kpn-location-page-header
            pageName="routes"
            pageTitle="Routes"
            i18n-pageTitle="@@location-routes.title"
          />

          <kpn-error />

          @if (service.response(); as response) {
            <div class="kpn-spacer-above">
              <kpn-location-response [response]="response">
                <kpn-location-routes [page]="response.result" />
              </kpn-location-response>
            </div>
          }
        </div>
      </div>
    </kpn-page>
  `,
  styles: `
    .page {
      display: flex;
      height: 100%;
    }

    .page-left {
      width: 20em;
      height: 100%;
      border-right: 1px solid lightgray;
      margin-right: 1em;
      overflow-y: scroll;
    }

    .page-right {
      flex-grow: 1;
    }
  `,
  providers: [LocationRoutesPageService, RouterService],
  imports: [
    ErrorComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    LocationRoutesComponent,
    LocationRoutesFilterComponent,
    PageComponent,
  ],
})
export class LocationRoutesPageComponent implements OnInit {
  protected readonly service = inject(LocationRoutesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
