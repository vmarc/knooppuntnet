import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { IconWayComponent } from '@app/components/shared/icon';
import { OsmLinkWayComponent } from '@app/components/shared/link';
import { ActionButtonWayComponent } from '../../../components/action/action-button-way.component';

@Component({
  selector: 'kpn-network-fact-way-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (elementId of elementIds(); track elementId) {
      <div class="kpn-align-center">
        <kpn-icon-way />
        <kpn-action-button-way [wayId]="elementId" />
        {{ elementId }}
      </div>
    }
  `,
  standalone: true,
  imports: [OsmLinkWayComponent, IconWayComponent, ActionButtonWayComponent],
})
export class NetworkFactWayIdsComponent {
  elementIds = input.required<number[]>();
}
