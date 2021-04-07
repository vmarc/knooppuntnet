import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { InterpretedTags } from '../../../components/shared/tags/interpreted-tags';

@Component({
  selector: 'kpn-network-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-data title="Situation on" i18n-title="@@network-details.situation-on">
      <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
    </kpn-data>

    <kpn-data title="Summary" i18n-title="@@network-details.summary">
      <kpn-network-summary [page]="response.result"></kpn-network-summary>
    </kpn-data>

    <kpn-data title="Country" i18n-title="@@network-details.country">
      <kpn-country-name
        [country]="response.result.attributes.country"
      ></kpn-country-name>
    </kpn-data>

    <kpn-data title="Last updated" i18n-title="@@network-details.last-updated">
      <kpn-timestamp
        [timestamp]="response.result.attributes.lastUpdated"
      ></kpn-timestamp>
    </kpn-data>

    <kpn-data
      title="Relation last updated"
      i18n-title="@@network-details.relation-last-updated"
    >
      <kpn-timestamp
        [timestamp]="response.result.attributes.relationLastUpdated"
      ></kpn-timestamp>
    </kpn-data>

    <kpn-data title="Tags" i18n-title="@@network-details.tags">
      <kpn-tags-table [tags]="tags"></kpn-tags-table>
    </kpn-data>
  `,
})
export class NetworkDetailsComponent implements OnInit {
  @Input() response: ApiResponse<NetworkDetailsPage>;

  tags: InterpretedTags;

  ngOnInit(): void {
    this.tags = InterpretedTags.networkTags(this.response.result.tags);
  }
}
