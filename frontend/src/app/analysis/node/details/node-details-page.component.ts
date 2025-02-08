import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { FactInfo } from '@app/analysis/fact';
import { FactsComponent } from '@app/analysis/fact';
import { DataComponent } from '@app/shared/components/data/data.component';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouterService } from '../../../shared/services/router.service';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeIntegrityComponent } from './components/node-integrity.component';
import { NodeLocationComponent } from './components/node-location.component';
import { NodeNetworkReferencesComponent } from './components/node-network-references.component';
import { NodeRouteReferencesComponent } from './components/node-route-references.component';
import { NodeSummaryComponent } from './components/node-summary.component';
import { NodeDetailsPageService } from './node-details-page.service';

@Component({
  selector: 'kpn-node-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
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

      <kpn-node-page-header pageName="details" />

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <div i18n="@@node.node-not-found">Node not found</div>
          }
          @if (response.result; as page) {
            <div>
              <kpn-data title="Summary" i18n-title="@@node.summary">
                <kpn-node-summary [nodeInfo]="page.nodeInfo" />
              </kpn-data>
              <div class="data2">
                <div class="title">
                  <span i18n="@@node.situation-on">Situation on</span>
                </div>
                <div class="body">
                  <kpn-timestamp [timestamp]="response.situationOn" />
                </div>
              </div>
              <kpn-data title="Last updated" i18n-title="@@node.last-updated">
                <kpn-timestamp [timestamp]="page.nodeInfo.lastUpdated" />
              </kpn-data>
              <kpn-data title="Tags" i18n-title="@@node.tags">
                <kpn-tag-table [tags]="buildTags(page)" />
              </kpn-data>
              <kpn-data title="Location" i18n-title="@@node.location">
                @if (routeTypes(); as routeTypes) {
                  <div>
                    @if (routeTypes.length > 1) {
                      <div>
                        @for (routeType of routeTypes; track routeType) {
                          <div class="kpn-line">
                            <nz-icon nzType="routeType" />
                            <kpn-node-location
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
                            <kpn-node-location
                              [routeType]="routeTypes[0]"
                              [locations]="page.nodeInfo.locations"
                            />
                          </div>
                        }
                      </div>
                    }
                  </div>
                }
              </kpn-data>
              <kpn-data title="Integrity" i18n-title="@@node.integrity">
                <kpn-node-integrity
                  [integrity]="page.integrity"
                  [mixedRouteScopes]="page.mixedRouteScopes"
                />
              </kpn-data>
              <kpn-data title="Routes" i18n-title="@@node.routes">
                <kpn-node-route-references
                  [references]="page.routeReferences"
                  [mixedRouteScopes]="page.mixedRouteScopes"
                />
              </kpn-data>
              <kpn-data title="Networks" i18n-title="@@node.networks">
                <kpn-node-network-references
                  [nodeInfo]="page.nodeInfo"
                  [references]="page.networkReferences"
                  [mixedRouteScopes]="page.mixedRouteScopes"
                />
              </kpn-data>
              <kpn-data title="Facts" i18n-title="@@node.facts">
                <kpn-facts [factInfos]="buildFactInfos(page)" />
              </kpn-data>
            </div>
          }
        </div>
      }
    </kpn-page>
  `,
  styleUrl: '../../../shared/components/data/data.component.scss',
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
