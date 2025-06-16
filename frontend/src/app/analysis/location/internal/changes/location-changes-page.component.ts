import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/shared/components/page/page.component';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { RouterService } from '@app/shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationChangesComponent } from './components/location-changes.component';
import { LocationChangesPageService } from './location-changes-page.service';

@Component({
  selector: 'ui-location-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-location-page-header
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@location-changes.title"
      />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <ui-location-response [response]="response">
            <ui-location-changes [page]="response.result" />
          </ui-location-response>
        </div>
      }
    </ui-page>
  `,
  providers: [LocationChangesPageService, RouterService],
  imports: [
    ErrorComponent,
    LocationChangesComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    PageComponent,
  ],
})
export class LocationChangesPageComponent implements OnInit {
  protected readonly service = inject(LocationChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
