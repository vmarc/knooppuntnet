import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { NetworkChangeSetComponent } from '@app/analysis/network/internal/changes/components/network-change-set.component';
import { NetworkChangesPageService } from '@app/analysis/network/internal/changes/network-changes-page.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';

@Component({
  selector: 'ui-network-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-changes [service]="service">
      @for (networkChangeInfo of changes(); track networkChangeInfo.rowIndex) {
        <ui-list-item [selected]="false">
          <ui-network-change-set [networkChangeInfo]="networkChangeInfo" />
        </ui-list-item>
      }
    </ui-changes>
  `,
  imports: [ChangesComponent, ListItemComponent, NetworkChangeSetComponent],
})
export class NetworkChangesComponent {
  protected readonly service = inject(NetworkChangesPageService);
  protected readonly changes = computed(() => this.service.response().result.changes);
}
