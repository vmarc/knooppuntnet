import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { FactsComponent } from '@app/analysis/fact/components/facts.component';
import { DataComponent } from '@app/shared/components/data/data.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouterService } from '@app/shared/services/router.service';
import { NodeIntegrityComponent } from './node-integrity.component';
import { NodeLocationComponent } from './node-location.component';
import { NodeNetworkReferencesComponent } from './node-network-references.component';
import { NodeRouteReferencesComponent } from './node-route-references.component';
import { NodeSummaryComponent } from './node-summary.component';
import { NodeDetailsPageService } from '../node-details-page.service';

@Component({
  selector: 'ui-node-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let page = response().result;
    <div>
      <ui-data title="Summary" i18n-title="@@node.summary">
        <ui-node-summary [nodeInfo]="page.nodeInfo" />
      </ui-data>
      <div class="data2">
        <div class="title">
          <span i18n="@@node.situation-on">Situation on</span>
        </div>
        <div class="body">
          <ui-timestamp [timestamp]="situationOn()" />
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
  `,
  styleUrl: '../../../../../shared/components/data/data.component.scss',
  providers: [RouterService],
  imports: [
    DataComponent,
    FactsComponent,
    NodeIntegrityComponent,
    NodeLocationComponent,
    NodeNetworkReferencesComponent,
    NodeRouteReferencesComponent,
    NodeSummaryComponent,
    NzIconDirective,
    TagTableComponent,
    TimestampComponent,
  ],
})
export class NodeDetailsComponent {
  private service = inject(NodeDetailsPageService);
  protected routeTypes = this.service.routeTypes;

  protected response = this.service.response;
  protected situationOn = computed(() => this.service.response().situationOn);

  buildTags(page: NodeDetailsPage) {
    return InterpretedTags.nodeTags(page.nodeInfo.tags);
  }

  buildFactInfos(page: NodeDetailsPage): FactInfo[] {
    return page.nodeInfo.facts.map((fact) => new FactInfo(fact));
  }
}
