import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {SubsetFactsPage} from "../../../kpn/api/common/subset/subset-facts-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {SubsetFactsPageService} from "./subset-facts-page.service";

@Component({
  selector: "kpn-subset-facts-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="service.subset | async"
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@subset-facts.title">
    </kpn-subset-page-header-block>

    <div *ngIf="service.response | async as response">
      <div>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </div>
      <div *ngIf="!hasFacts(response)">
        <kpn-icon-happy></kpn-icon-happy>
        <span i18n="@@subset-facts.no-facts">No facts</span>
      </div>
      <div *ngIf="hasFacts(response)" class="kpn-line">
        <kpn-items>
          <kpn-item *ngFor="let factCount of response.result.factCounts; let i=index" [index]="i">
            <a [routerLink]="factCount.fact.name">
              <kpn-fact-name [factName]="factCount.fact.name"></kpn-fact-name>
            </a>
            ({{factCount.count}})
            <kpn-fact-description [factName]="factCount.fact.name"></kpn-fact-description>
          </kpn-item>
        </kpn-items>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `,
  providers: [
    SubsetFactsPageService
  ]
})
export class SubsetFactsPageComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute,
              public service: SubsetFactsPageService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(first()).subscribe(params => this.service.params(params));
  }

  hasFacts(response: ApiResponse<SubsetFactsPage>): boolean {
    return response.result && response.result.subsetInfo.factCount > 0;
  }

}
