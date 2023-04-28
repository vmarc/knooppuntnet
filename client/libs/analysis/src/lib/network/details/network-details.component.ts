import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { InterpretedTags } from '@app/components/shared/tags';

@Component({
  selector: 'kpn-network-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-data title="Summary" i18n-title="@@network-details.summary">
      <kpn-network-summary [page]="response.result" />
    </kpn-data>

    <div class="data2">
      <div class="title">
        <span i18n="@@network-details.situation-on">Situation on</span>
      </div>
      <div class="body">
        <kpn-timestamp [timestamp]="response.situationOn" />
      </div>
    </div>

    <div class="data2">
      <div class="title">
        <span i18n="@@network-details.last-updated">Last updated</span>
      </div>
      <div class="body">
        <kpn-timestamp [timestamp]="response.result.attributes.lastUpdated" />
      </div>
    </div>

    <kpn-data
      title="Relation last updated"
      i18n-title="@@network-details.relation-last-updated"
    >
      <kpn-timestamp
        [timestamp]="response.result.attributes.relationLastUpdated"
      />
    </kpn-data>

    <kpn-data title="Tags" i18n-title="@@network-details.tags">
      <kpn-tags-table [tags]="tags" />
    </kpn-data>
  `,
  styleUrls: [
    '../../../../../shared/src/lib/components/shared/data/data.component.scss',
  ],
})
export class NetworkDetailsComponent implements OnInit {
  @Input() response: ApiResponse<NetworkDetailsPage>;

  tags: InterpretedTags;

  ngOnInit(): void {
    this.tags = InterpretedTags.networkTags(this.response.result.tags);
  }
}
