import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {ChangeSetPage} from "../../../kpn/api/common/changes/change-set-page";
import {Subscriptions} from "../../../util/Subscriptions";
import {flatMap, map} from "rxjs/operators";

class ChangeSetKey {
  constructor(readonly changeSetId: string,
              readonly replicationNumber: string) {
  }
}

@Component({
  selector: "kpn-change-set-page",
  template: `
      <h1>
          <ng-container i18n="@@change-set.title">Changeset</ng-container>
          {{changeSetTitle()}}
      </h1>

      <div *ngIf="response">
          <kpn-change-set-header [page]="response.result"></kpn-change-set-header>
          <kpn-change-set-network-changes [page]="response.result"></kpn-change-set-network-changes>
          <kpn-change-set-orphan-node-changes [page]="response.result"></kpn-change-set-orphan-node-changes>
          <kpn-change-set-orphan-route-changes [page]="response.result"></kpn-change-set-orphan-route-changes>
          <kpn-json [object]="response"></kpn-json>
      </div>
  `
})
export class ChangeSetPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  response: ApiResponse<ChangeSetPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => this.interpreteParams(params)),
        flatMap(key => this.appService.changeSet(key.changeSetId, key.replicationNumber))
      ).subscribe(response => this.response = response)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  changeSetTitle() {
    if (this.response) {
      const a = this.response.result.summary.key.changeSetId;
      const b = Util.replicationName(this.response.result.summary.key.replicationNumber);
      return a + " " + b;
    }
    return "";
  }

  private interpreteParams(params: Params): ChangeSetKey {
    const changeSetId = params["changeSetId"];
    const replicationNumber = params["replicationNumber"];
    return new ChangeSetKey(changeSetId, replicationNumber);
  }

}
