import {Component, Input, OnInit} from "@angular/core";
import {InterpretedTags} from "../../../components/shared/tags/interpreted-tags";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NetworkDetailsPage} from "../../../kpn/shared/network/network-details-page";

@Component({
  selector: "kpn-network-details",
  template: `
    <kpn-data title="Situation on" i18n-title="@@network-details.situation-on">
      <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
    </kpn-data>

    <kpn-data title="Summary" i18n-title="@@network-details.summary">
      <kpn-network-summary [page]="response.result"></kpn-network-summary>
    </kpn-data>

    <kpn-data title="Country" i18n-title="@@network-details.country">
      <kpn-country-name [country]="response.result.attributes.country"></kpn-country-name>
    </kpn-data>

    <kpn-data title="Last updated" i18n-title="@@network-details.last-updated">
      <kpn-timestamp [timestamp]="response.result.attributes.lastUpdated"></kpn-timestamp>
    </kpn-data>

    <kpn-data title="Relation last updated" i18n-title="@@network-details.relation-last-updated">
      <kpn-timestamp [timestamp]="response.result.attributes.relationLastUpdated"></kpn-timestamp>
    </kpn-data>

    <kpn-data title="Tags" i18n-title="@@network-details.tags">
      <kpn-tags-table [tags]="tags"></kpn-tags-table>
    </kpn-data>
  `,
  styles: []
})
export class NetworkDetailsComponent implements OnInit {

  @Input() response: ApiResponse<NetworkDetailsPage>;

  tags: InterpretedTags;

  ngOnInit() {
    this.tags = InterpretedTags.networkTags(this.response.result.tags);
  }

}
