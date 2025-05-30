import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { FactsComponent } from '@app/analysis/fact/components/facts.component';
import { DataComponent } from '@app/shared/components/data/data.component';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouterService } from '@app/shared/services/router.service';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeIntegrityComponent } from './components/node-integrity.component';
import { NodeLocationComponent } from './components/node-location.component';
import { NodeNetworkReferencesComponent } from './components/node-network-references.component';
import { NodeRouteReferencesComponent } from './components/node-route-references.component';
import { NodeSummaryComponent } from './components/node-summary.component';
import { NodeDetailsPageService } from './node-details-page.service';

@Component({
  selector: 'ui-node-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.node">Node</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <ui-node-page-header pageName="details" />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <div i18n="@@node.node-not-found">Node not found</div>
          }
          @if (response.result; as page) {
            <div>
              <ui-data title="Summary" i18n-title="@@node.summary">
                <ui-node-summary [nodeInfo]="page.nodeInfo" />
              </ui-data>
              <div class="data2">
                <div class="title">
                  <span i18n="@@node.situation-on">Situation on</span>
                </div>
                <div class="body">
                  <ui-timestamp [timestamp]="response.situationOn" />
                </div>
              </div>
              <ui-data title="Last updated" i18n-title="@@node.last-updated">
                <ui-timestamp [timestamp]="page.nodeInfo.lastUpdated" />
              </ui-data>
              <ui-data title="Tags" i18n-title="@@node.tags">
                <ui-tag-table [tags]="buildTags(page)" />
              </ui-data>
              <ui-data title="Location" i18n-title="@@node.location">
                @if (routeTypes(); as routeTypes) {
                  <div>
                    @if (routeTypes.length > 1) {
                      <div>
                        @for (routeType of routeTypes; track routeType) {
                          <div class="kpn-line">
                            <nz-icon [nzType]="routeType" />
                            <ui-node-location
                              [routeType]="routeType"
                              [locations]="page.nodeInfo.locations"
                            />
                          </div>
                        }
                      </div>
                    }
                    @if (routeTypes.length === 1) {
                      <div>
                        @for (routeType of routeTypes; track routeType) {
                          <div>
                            <ui-node-location
                              [routeType]="routeTypes[0]"
                              [locations]="page.nodeInfo.locations"
                            />
                          </div>
                        }
                      </div>
                    }
                  </div>
                }
              </ui-data>
              <ui-data title="Integrity" i18n-title="@@node.integrity">
                <ui-node-integrity
                  [integrity]="page.integrity"
                  [mixedRouteScopes]="page.mixedRouteScopes"
                />
              </ui-data>
              <ui-data title="Routes" i18n-title="@@node.routes">
                <ui-node-route-references
                  [references]="page.routeReferences"
                  [mixedRouteScopes]="page.mixedRouteScopes"
                />
              </ui-data>
              <ui-data title="Networks" i18n-title="@@node.networks">
                <ui-node-network-references
                  [nodeInfo]="page.nodeInfo"
                  [references]="page.networkReferences"
                  [mixedRouteScopes]="page.mixedRouteScopes"
                />
              </ui-data>
              <ui-data title="Facts" i18n-title="@@node.facts">
                <ui-facts [factInfos]="buildFactInfos(page)" />
              </ui-data>
            </div>
          }
        </div>
      }
    </ui-page>
  `,
  styleUrl: '../../../../shared/components/data/data.component.scss',
  providers: [NodeDetailsPageService, RouterService],
  imports: [
    DataComponent,
    ErrorComponent,
    FactsComponent,
    NodeIntegrityComponent,
    NodeLocationComponent,
    NodeNetworkReferencesComponent,
    NodePageHeaderComponent,
    NodeRouteReferencesComponent,
    NodeSummaryComponent,
    NzIconDirective,
    PageComponent,
    RouterLink,
    TagTableComponent,
    TimestampComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
  ],
})
export class NodeDetailsPageComponent implements OnInit {
  readonly service = inject(NodeDetailsPageService);
  readonly routeTypes = this.service.routeTypes;

  ngOnInit(): void {
    this.service.onInit();
  }

  buildTags(page: NodeDetailsPage) {
    return InterpretedTags.nodeTags(page.nodeInfo.tags);
  }

  buildFactInfos(page: NodeDetailsPage): FactInfo[] {
    return page.nodeInfo.facts.map((fact) => new FactInfo(fact));
  }
}
