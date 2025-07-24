import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { SubsetOrphanRouteListComponent } from './components/subset-orphan-route-list.component';
import { SubsetOrphanRoutesPageService } from './subset-orphan-routes-page.service';

@Component({
  selector: 'ui-subset-orphan-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
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
  imports: [SituationOnComponent, SubsetOrphanRouteListComponent],
})
export class SubsetOrphanRoutesPageComponent implements OnInit {
  protected readonly service = inject(SubsetOrphanRoutesPageService);
  protected readonly filterOptions = this.service.filterOptions;

  ngOnInit(): void {
    this.service.onInit();
  }
}
