import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { ChangeOption } from '@app/shared/kpn/common/change-option';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageFilterComponent } from '@app/shared/components/page/page-filter.component';
import { RouterService } from '@app/shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationChangesComponent } from './components/location-changes.component';
import { LocationChangesPageService } from './location-changes-page.service';

@Component({
  selector: 'kpn-location-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-filter>
      <kpn-location-page-header
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@location-changes.title"
      />

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <kpn-location-response [response]="response">
            <kpn-location-changes [page]="response.result" />
          </kpn-location-response>
        </div>
      }
      <kpn-change-filter
        [filterOptions]="service.filterOptions()"
        (optionSelected)="onOptionSelected($event)"
        filter
      />
    </kpn-page-filter>
  `,
  providers: [LocationChangesPageService, RouterService],
  imports: [
    ErrorComponent,
    LocationChangesComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    PageFilterComponent,
    ChangeFilterComponent,
  ],
})
export class LocationChangesPageComponent implements OnInit {
  protected readonly service = inject(LocationChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  onOptionSelected(option: ChangeOption): void {
    this.service.setFilterOption(option);
  }
}
