import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { NzTabSetComponent } from 'ng-zorro-antd/tabs';
import { NzTabComponent } from 'ng-zorro-antd/tabs';
import { PageComponent } from '../../../shared/components/shared/page/page.component';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkNodeTableComponent } from './components/network-node-table.component';
import { NetworkNodesPageService } from './network-nodes-page.service';

@Component({
  selector: 'kpn-network-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-network-page-header
        pageName="nodes"
        pageTitle="Nodes"
        i18n-pageTitle="@@network-nodes.title"
      />

      <nz-tabset [nzSelectedIndex]="selectedTabIndex()" nzSize="small">
        <nz-tab nzTitle="Nodes">
          @if (service.response(); as response) {
            @if (!response.result) {
              <p i18n="@@network-page.network-not-found">Network not found</p>
            } @else {
              @if (response.result; as page) {
                <p>
                  <kpn-situation-on [timestamp]="response.situationOn" />
                </p>
                @if (page.nodes.length === 0) {
                  <div i18n="@@network-nodes.no-nodes">No network nodes in network</div>
                } @else {
                  <kpn-network-node-table
                    [routeType]="page.summary.routeType"
                    [routeScope]="page.summary.routeScope"
                    [timeInfo]="page.timeInfo"
                    [surveyDateInfo]="page.surveyDateInfo"
                    [nodes]="page.nodes"
                  />
                }
              }
            }
          }
        </nz-tab>
        <nz-tab nzTitle="Filter">
          <kpn-filter [filterOptions]="service.filterOptions()" />
        </nz-tab>
      </nz-tabset>
    </kpn-page>
  `,
  providers: [NetworkNodesPageService, RouterService],
  imports: [
    FilterComponent,
    NetworkNodeTableComponent,
    NetworkPageHeaderComponent,
    PageComponent,
    SituationOnComponent,
    NzTabComponent,
    NzTabSetComponent,
  ],
})
export class NetworkNodesPageComponent implements OnInit {
  protected readonly service = inject(NetworkNodesPageService);
  readonly selectedTabIndex = this.service.selectedTabIndex;

  ngOnInit(): void {
    this.service.onInit();
  }
}
