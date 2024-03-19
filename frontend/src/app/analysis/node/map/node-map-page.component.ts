import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeDetailsSidebarComponent } from '../details/components/node-details-sidebar.component';
import { NodeMapComponent } from './components/node-map.component';
import { NodeMapService } from './components/node-map.service';
import { NodeMapPageService } from './node-map-page.service';

@Component({
  selector: 'kpn-node-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.node-map">Node map</li>
      </ul>

      <kpn-node-page-header pageName="map" />

      <kpn-error />

      @if (service.response(); as response) {
        <div>
          @if (!response.result) {
            <div class="kpn-spacer-above" i18n="@@node.node-not-found">Node not found</div>
          } @else {
            <kpn-node-map />
          }
        </div>
      }
      <kpn-node-details-sidebar sidebar />
    </kpn-page>
  `,
  providers: [NodeMapPageService, NodeMapService, RouterService],
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    NodeDetailsSidebarComponent,
    NodeMapComponent,
    NodePageHeaderComponent,
    PageComponent,
    RouterLink,
  ],
})
export class NodeMapPageComponent implements OnInit {
  protected readonly service = inject(NodeMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
