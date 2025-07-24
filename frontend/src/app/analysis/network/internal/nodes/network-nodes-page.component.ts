import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { NetworkNodeListComponent } from './components/network-node-list.component';
import { NetworkNodesPageService } from './network-nodes-page.service';

@Component({
  selector: 'ui-network-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      @if (response.result; as page) {
        <p class="kpn-spacer-above">
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
  `,
  providers: [NetworkNodesPageService],
  imports: [NetworkNodeListComponent, SituationOnComponent],
})
export class NetworkNodesPageComponent implements OnInit {
  protected readonly service = inject(NetworkNodesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
