import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkNodeListComponent } from './components/network-node-list.component';
import { NetworkNodesPageService } from './network-nodes-page.service';

@Component({
  selector: 'ui-network-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-network-page-header
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
              <ui-situation-on [timestamp]="response.situationOn" />
            </p>
            @if (page.nodes.length === 0) {
              <div i18n="@@network-nodes.no-nodes">No network nodes in network</div>
            } @else {
              <ui-network-node-list
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
    </ui-page>
  `,
  providers: [NetworkNodesPageService, RouterService],
  imports: [
    NetworkNodeListComponent,
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
