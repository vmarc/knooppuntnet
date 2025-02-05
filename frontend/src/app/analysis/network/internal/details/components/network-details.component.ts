import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { DataComponent } from '@app/shared/components/data/data.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { NetworkSummaryComponent } from './network-summary.component';

@Component({
  selector: 'kpn-network-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-data title="Summary" i18n-title="@@network-details.summary">
      <kpn-network-summary [page]="response().result" />
    </kpn-data>

    <div class="data2">
      <div class="title">
        <span i18n="@@network-details.situation-on">Situation on</span>
      </div>
      <div class="body">
        <kpn-timestamp [timestamp]="response().situationOn" />
      </div>
    </div>

    <div class="data2">
      <div class="title">
        <span i18n="@@network-details.last-updated">Last updated</span>
      </div>
      <div class="body">
        <kpn-timestamp [timestamp]="response().result.attributes.lastUpdated" />
      </div>
    </div>

    <kpn-data title="Relation last updated" i18n-title="@@network-details.relation-last-updated">
      <kpn-timestamp [timestamp]="response().result.attributes.relationLastUpdated" />
    </kpn-data>

    <kpn-data title="Tags" i18n-title="@@network-details.tags">
      <kpn-tag-table [tags]="tags" />
    </kpn-data>
  `,
  styleUrl: '../../../../../shared/components/data/data.component.scss',
  imports: [DataComponent, NetworkSummaryComponent, TagTableComponent, TimestampComponent],
})
export class NetworkDetailsComponent implements OnInit {
  response = input.required<ApiResponse<NetworkDetailsPage>>();

  tags: InterpretedTags;

  ngOnInit(): void {
    this.tags = InterpretedTags.networkTags(this.response().result.tags);
  }
}
