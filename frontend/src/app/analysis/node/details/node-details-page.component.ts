import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NodeDetailsPage } from '@api/common/node';
import { FactInfo } from '@app/analysis/fact';
import { FactsComponent } from '@app/analysis/fact';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { DataComponent } from '@app/components/shared/data';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../../shared/services/router.service';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
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
      <ul class="breadcrumb">
        <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.node">Node</li>
      </ul>

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
                <kpn-tags-table [tags]="buildTags(page)" />
              </kpn-data>
              <kpn-data title="Location" i18n-title="@@node.location">
                @if (networkTypes(); as networkTypes) {
                  <div>
                    @if (networkTypes.length > 1) {
                      <div>
                        @for (networkType of networkTypes; track networkType) {
                          <div class="kpn-line">
                            <kpn-network-type-icon [networkType]="networkType" />
                            <kpn-node-location
                              [networkType]="networkType"
                              [locations]="page.nodeInfo.locations"
                            />
                          </div>
                        }
                      </div>
                    }
                    @if (networkTypes.length === 1) {
                      <div>
                        @for (networkType of networkTypes; track networkType) {
                          <div>
                            <kpn-node-location
                              [networkType]="networkTypes[0]"
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
                  [mixedNetworkScopes]="page.mixedNetworkScopes"
                />
              </kpn-data>
              <kpn-data title="Routes" i18n-title="@@node.routes">
                <kpn-node-route-references
                  [references]="page.routeReferences"
                  [mixedNetworkScopes]="page.mixedNetworkScopes"
                />
              </kpn-data>
              <kpn-data title="Networks" i18n-title="@@node.networks">
                <kpn-node-network-references
                  [nodeInfo]="page.nodeInfo"
                  [references]="page.networkReferences"
                  [mixedNetworkScopes]="page.mixedNetworkScopes"
                />
              </kpn-data>
              <kpn-data title="Facts" i18n-title="@@node.facts">
                <kpn-facts [factInfos]="buildFactInfos(page)" />
              </kpn-data>
            </div>
          }
        </div>
      }
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  styleUrl: '../../../shared/components/shared/data/data.component.scss',
  providers: [NodeDetailsPageService, RouterService],
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    DataComponent,
    ErrorComponent,
    FactsComponent,
    NetworkTypeIconComponent,
    NodeIntegrityComponent,
    NodeLocationComponent,
    NodeNetworkReferencesComponent,
    NodePageHeaderComponent,
    NodeRouteReferencesComponent,
    NodeSummaryComponent,
    PageComponent,
    RouterLink,
    TagsTableComponent,
    TimestampComponent,
  ],
})
export class NodeDetailsPageComponent implements OnInit {
  protected readonly service = inject(NodeDetailsPageService);
  protected readonly networkTypes = this.service.networkTypes;

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
