import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { NodeChangeComponent } from '@app/analysis/node/internal/changes/components/node-change.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { RouterService } from '@app/shared/services/router.service';
import { NodeChangesPageService } from '../node-changes-page.service';

@Component({
  selector: 'ui-node-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-changes [service]="service">
      @for (nodeChangeInfo of service.response().result.changes; track nodeChangeInfo.id) {
        <ui-list-item [selected]="false">
          <ui-node-change [nodeChangeInfo]="nodeChangeInfo" />
        </ui-list-item>
      }
    </ui-changes>
  `,
  providers: [RouterService],
  imports: [NodeChangeComponent, ChangesComponent, ListItemComponent],
})
export class NodeChangesComponent {
  protected readonly service = inject(NodeChangesPageService);
}
