import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../app.service";
import {PageService} from "../../components/shared/page.service";
import {ApiResponse} from "../../kpn/shared/api-response";
import {ChangesPage} from "../../kpn/shared/changes-page";
import {ChangesParameters} from "../../kpn/shared/changes/filter/changes-parameters";
import {Subscriptions} from "../../util/Subscriptions";

@Component({
  selector: "kpn-changes-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <span i18n="@@breadcrumb.changes">Changes</span>
    </div>

    <kpn-page-header subject="changes-page" i18n="@@changes-page.title">Changes</kpn-page-header>

    <div *ngIf="response">

      <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>

      <kpn-changes [(parameters)]="parameters" [totalCount]="page.changeCount" [showFirstLastButtons]="false">
        <kpn-items>
          <kpn-item *ngFor="let changeSet of page.changes; let i=index" [index]="rowIndex(i)">
            <kpn-change-set [changeSet]="changeSet"></kpn-change-set>
          </kpn-item>
        </kpn-items>
      </kpn-changes>

      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class ChangesPageComponent implements OnInit {

  private readonly subscriptions = new Subscriptions();

  response: ApiResponse<ChangesPage>;
  private _parameters = new ChangesParameters(null, null, null, null, null, null, null, 15, 0, true);

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.reload();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get page(): ChangesPage {
    return this.response.result;
  }

  get parameters() {
    return this._parameters;
  }

  set parameters(parameters: ChangesParameters) {
    this._parameters = parameters;
    this.reload();
  }

  rowIndex(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index;
  }

  private reload() {
    this.appService.changes(this.parameters).subscribe(response => {
      this.response = response;
    });
  }

}
