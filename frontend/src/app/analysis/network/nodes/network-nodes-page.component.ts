import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkNodeTableComponent } from './components/network-node-table.component';
import { NetworkNodesSidebarComponent } from './components/network-nodes-sidebar.component';
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
                [networkType]="page.summary.networkType"
                [networkScope]="page.summary.networkScope"
                [timeInfo]="page.timeInfo"
                [surveyDateInfo]="page.surveyDateInfo"
                [nodes]="page.nodes"
              />
            }
          }
        }
      }
      <kpn-network-nodes-sidebar sidebar />
    </kpn-page>
  `,
  providers: [NetworkNodesPageService, RouterService],
  standalone: true,
  imports: [
    NetworkNodeTableComponent,
    NetworkNodesSidebarComponent,
    NetworkPageHeaderComponent,
    PageComponent,
    SituationOnComponent,
  ],
})
export class NetworkNodesPageComponent implements OnInit {
  protected readonly service = inject(NetworkNodesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
