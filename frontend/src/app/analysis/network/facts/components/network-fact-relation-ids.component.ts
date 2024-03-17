import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { IconRouteComponent } from '@app/components/shared/icon';
import { LinkRouteComponent } from '@app/components/shared/link';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';

@Component({
  selector: 'kpn-network-fact-relation-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (elementId of elementIds(); track elementId) {
      <div class="kpn-align-center">
        <kpn-icon-route />
        <kpn-action-button-route [relationId]="elementId" />
        {{ elementId }}
      </div>
    }
  `,
  standalone: true,
  imports: [ActionButtonRouteComponent, IconRouteComponent, LinkRouteComponent],
})
export class NetworkFactRelationIdsComponent {
  elementIds = input.required<number[]>();
}
