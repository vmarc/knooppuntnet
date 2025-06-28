import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { CountryNameComponent } from '@app/shared/components/country-name.component';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { IconWarningComponent } from '@app/shared/components/icon/icon-warning.component';
import { MarkdownComponent } from 'ngx-markdown';
import { ActionButtonRelationComponent } from '../../../../components/action/action-button-relation.component';

@Component({
  selector: 'ui-network-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!page().active) {
      <p class="kpn-warning" i18n="@@network-details.not-active">
        This network is not active anymore.
      </p>
    } @else {
      <p class="kpn-comma-list">
        <span>
          <!-- nested spans needed to combine the ::after's in kpn-comma-list and kpn-km -->
          <span class="kpn-km">{{ page().attributes.km | integer }}</span>
        </span>
        <span>
          {{ page().summary.nodeCount | integer }}
          <ng-container i18n="@@network-details.nodes">nodes</ng-container>
        </span>
        <span>
          {{ page().summary.routeCount | integer }}
          <ng-container i18n="@@network-details.routes">routes</ng-container>
        </span>
      </p>
    }

    <p>
      <ui-country-name [country]="page().attributes.country" />
    </p>

    @if (page().active && page().attributes.brokenRouteCount > 0) {
      <p class="kpn-line">
        <ui-icon-warning />
        <span i18n="@@network-details.contains-broken-routes">
          This network contains broken (non-continuous) routes.
        </span>
      </p>
    }

    @if (page().active && isProposed()) {
      <p class="kpn-line">
        <ui-icon-warning />
        <markdown i18n="@@network.proposed">
          Proposed: this network has tag _"state=proposed"_. The network is assumed to still be in a
          planning phase and likely not signposted in the field.
        </markdown>
      </p>
    }

    <div class="kpn-line">
      {{ page().attributes.id }}
      <ui-action-button-relation [relationId]="page().attributes.id" />
    </div>
  `,
  imports: [
    ActionButtonRelationComponent,
    CountryNameComponent,
    IconWarningComponent,
    IntegerFormatPipe,
    IntegerFormatPipe,
    MarkdownComponent,
  ],
})
export class NetworkSummaryComponent {
  readonly page = input.required<NetworkDetailsPage>();

  isProposed() {
    const stateTag = this.page().tags.find((t) => t.key === 'state');
    return stateTag && stateTag.value === 'proposed';
  }
}
