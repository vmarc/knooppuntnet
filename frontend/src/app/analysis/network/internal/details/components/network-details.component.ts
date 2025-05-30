import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { DataComponent } from '@app/shared/components/data/data.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { NetworkSummaryComponent } from './network-summary.component';

@Component({
  selector: 'ui-network-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-data title="Summary" i18n-title="@@network-details.summary">
      <ui-network-summary [page]="response().result" />
    </ui-data>

    <div class="data2">
      <div class="title">
        <span i18n="@@network-details.situation-on">Situation on</span>
      </div>
      <div class="body">
        <ui-timestamp [timestamp]="response().situationOn" />
      </div>
    </div>

    <div class="data2">
      <div class="title">
        <span i18n="@@network-details.last-updated">Last updated</span>
      </div>
      <div class="body">
        <ui-timestamp [timestamp]="response().result.attributes.lastUpdated" />
      </div>
    </div>

    <ui-data title="Relation last updated" i18n-title="@@network-details.relation-last-updated">
      <ui-timestamp [timestamp]="response().result.attributes.relationLastUpdated" />
    </ui-data>

    <ui-data title="Tags" i18n-title="@@network-details.tags">
      <ui-tag-table [tags]="tags" />
    </ui-data>
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
