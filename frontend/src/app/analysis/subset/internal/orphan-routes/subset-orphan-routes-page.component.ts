import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetOrphanRouteListComponent } from './components/subset-orphan-route-list.component';
import { SubsetOrphanRoutesPageService } from './subset-orphan-routes-page.service';

@Component({
  selector: 'ui-subset-orphan-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-subset-page-header-block
      pageName="orphan-routes"
      pageTitle="Free routes"
      i18n-pageTitle="@@subset-orphan-routes.title"
    />

    <ui-error />

    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        <p>
          <ui-situation-on [timestamp]="response.situationOn" />
        </p>
        @if (response.result.routes.length === 0) {
          <p class="kpn-line">
            <span i18n="@@subset-orphan-routes.no-routes">No free routes</span>
          </p>
        } @else {
          <ui-subset-orphan-route-list />
        }
      </div>
    }
  `,
  providers: [SubsetOrphanRoutesPageService],
  imports: [
    ErrorComponent,
    SituationOnComponent,
    SubsetOrphanRouteListComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetOrphanRoutesPageComponent implements OnInit {
  protected readonly service = inject(SubsetOrphanRoutesPageService);
  protected readonly filterOptions = this.service.filterOptions;

  ngOnInit(): void {
    this.service.onInit();
  }
}
