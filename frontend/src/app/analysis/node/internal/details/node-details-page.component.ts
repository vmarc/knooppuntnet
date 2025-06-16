import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NodeDetailsComponent } from '@app/analysis/node/internal/details/components/node-details.component';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeDetailsPageService } from './node-details-page.service';

@Component({
  selector: 'ui-node-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-node-page-header pageName="details" />
      <ui-error />
      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <div i18n="@@node.node-not-found">Node not found</div>
          }
          @if (response.result; as page) {
            <ui-node-details />
          }
        </div>
      }
    </ui-page>
  `,
  providers: [NodeDetailsPageService, RouterService],
  imports: [ErrorComponent, NodeDetailsComponent, NodePageHeaderComponent, PageComponent],
})
export class NodeDetailsPageComponent implements OnInit {
  protected service = inject(NodeDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
