import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { RouteDetailsComponent } from '@app/analysis/route/internal/details/components/route-details.component';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteDetailsPageService } from './route-details-page.service';

@Component({
  selector: 'ui-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header pageName="details" />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <div i18n="@@route.route-not-found">Route not found</div>
          }
          @if (response.result; as page) {
            <ui-route-details />
          }
        </div>
      }
    </ui-page>
  `,
  providers: [RouteDetailsPageService, RouterService],
  imports: [PageComponent, RoutePageHeaderComponent, RouteDetailsComponent],
})
export class RouteDetailsPageComponent implements OnInit {
  readonly service = inject(RouteDetailsPageService);
  private readonly pageWidthService = inject(PageWidthService);

  readonly showRouteDetails = computed(() => !this.pageWidthService.isAllSmall());

  ngOnInit(): void {
    this.service.onInit();
  }

  routeTags(page: RouteDetailsPage) {
    return InterpretedTags.routeTags(page.route.summary.tags);
  }

  factInfos(page: RouteDetailsPage): FactInfo[] {
    return page.route.facts.map((fact) => {
      if (fact === 'RouteUnexpectedNode') {
        const unexpectedNodeIds = page.route.unexpectedNodeIds;
        return new FactInfo(fact, undefined, undefined, undefined, unexpectedNodeIds);
      }
      if (fact === 'RouteUnexpectedRelation') {
        const unexpectedRelationIds = page.route.unexpectedRelationIds;
        return new FactInfo(
          fact,
          undefined,
          undefined,
          undefined,
          undefined,
          unexpectedRelationIds
        );
      }
      return new FactInfo(fact);
    });
  }
}
