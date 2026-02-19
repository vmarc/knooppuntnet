import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { IconRouteComponent } from '@app/shared/components/icon/icon-route.component';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';

@Component({
  selector: 'ui-network-fact-relation-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (elementId of elementIds(); track elementId) {
      <div class="kpn-align-center kpn-line">
        <ui-icon-route />
        <ui-action-button-route [relationId]="elementId" />
        {{ elementId }}
      </div>
    }
  `,
  imports: [ActionButtonRouteComponent, IconRouteComponent],
})
export class NetworkFactRelationIdsComponent {
  readonly elementIds = input.required<ReadonlyArray<number>>();
}
