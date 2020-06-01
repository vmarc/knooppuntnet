import {Input} from "@angular/core";
import {ChangeDetectionStrategy, Component, OnInit} from "@angular/core";
import {List} from "immutable";
import {concat} from "rxjs";
import {Observable} from "rxjs";
import {delay} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationSummaryPage} from "../../../kpn/api/common/location/location-summary-page";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-location-summary",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-data title="Summary" i18n-title="@@location-summary.summary">
      <p>{{page.summary.nodeCount}} <span>nodes</span></p>
      <p>{{page.summary.routeCount}} <span>routes</span></p>
      <p>{{page.summary.factCount}} <span>facts</span></p>
      <p>
        <a rel="nofollow" (click)="edit()" title="Open in editor (like JOSM)" i18n="@@links.edit">edit</a>
      </p>
    </kpn-data>
  `
})
export class LocationSummaryComponent {

  @Input() page: LocationSummaryPage;

  progressCount = 0;
  progress$: Observable<number>;
  private apiUrl = "http://localhost:8111/import?url=https://api.openstreetmap.org/api/0.6";

  constructor(private appService: AppService) {
  }

  edit(): void {

    const nodeEdits: List<Observable<Object>> = this.page.nodeIds.map(nodeIds => {
      const nodeIdString = nodeIds.ids.join(",");
      const url = `${this.apiUrl}/nodes?nodes=${nodeIdString}`;
      return this.appService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(500)
      );
    });

    const allRouteIds = this.page.routeIds.flatMap(ids => ids.ids).take(4);

    const routeEdits = allRouteIds.map(routeId => {
      const url = `${this.apiUrl}/relation/${routeId}/full`;
      return this.appService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(500)
      );
    });

    const allEdits = nodeEdits.concat(routeEdits).toArray();

    concat(...allEdits).subscribe(
      result => {
        console.log(["RESULT", result]);
      },
      err => {
        console.log(["ERROR", err]);
      },
      () => console.log("all done")
    );
  }

  private updateProgress() {
    this.progressCount = this.progressCount + 1;
    console.log("update progress " + this.progressCount);
  }
}
