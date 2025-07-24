import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NodeDetailsComponent } from '@app/analysis/node/internal/details/components/node-details.component';
import { RouterService } from '@app/shared/services/router.service';
import { NodeDetailsPageService } from './node-details-page.service';

@Component({
  selector: 'ui-node-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response()?.result) {
      <div class="kpn-spacer-above">
        <ui-node-details />
      </div>
    }
  `,
  providers: [NodeDetailsPageService, RouterService],
  imports: [NodeDetailsComponent],
})
export class NodeDetailsPageComponent implements OnInit {
  protected service = inject(NodeDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
