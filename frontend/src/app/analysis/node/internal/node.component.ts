import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NodePageHeaderComponent } from '@app/analysis/node/internal/components/node-page-header.component';
import { NodeService } from '@app/analysis/node/internal/node.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';

@Component({
  selector: 'ui-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-node-page-header />
      <ui-error />
      @if (nodeNotFound()) {
        <div class="kpn-spacer-above">
          <div i18n="@@node.node-not-found">Node not found</div>
        </div>
      }
      <router-outlet />
    </ui-page>
  `,
  imports: [RouterOutlet, PageComponent, ErrorComponent, NodePageHeaderComponent],
})
export class NodeComponent implements OnInit {
  private nodeService = inject(NodeService);
  readonly nodeId = input<number>();
  protected nodeNotFound = this.nodeService.nodeNotFound;

  ngOnInit(): void {
    this.nodeService.onInit(this.nodeId());
  }
}
