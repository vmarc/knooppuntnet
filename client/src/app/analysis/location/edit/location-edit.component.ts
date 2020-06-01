import {Input} from "@angular/core";
import {ChangeDetectionStrategy, Component} from "@angular/core";
import {List, Range} from "immutable";
import {BehaviorSubject} from "rxjs";
import {concat} from "rxjs";
import {Observable} from "rxjs";
import {delay} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationEditPage} from "../../../kpn/api/common/location/location-edit-page";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-location-edit",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      {{page.summary.nodeCount}} <span>nodes, </span>
      {{page.summary.routeCount}} <span>routes</span>
    </p>
    <p>
      <a rel="nofollow" (click)="edit()" title="Open in editor (like JOSM)" i18n="@@links.edit">edit</a>
    </p>
    <p *ngIf="showProgress$ | async">
      <mat-progress-bar [value]="progress$ | async"></mat-progress-bar>
    </p>
    <p>
      {{action$ | async}}
    </p>
    <p *ngIf="ready$ | async">
      Ready
    </p>
  `
})
export class LocationEditComponent {

  @Input() page: LocationEditPage;

  progressCount = 0;
  progressSteps = 0;

  action$ = new BehaviorSubject<string>("");
  progress$ = new BehaviorSubject<number>(0);
  showProgress$ = new BehaviorSubject<boolean>(false);
  ready$ = new BehaviorSubject<boolean>(false);

  private josmUrl = "http://localhost:8111/";
  private apiUrl = this.josmUrl + "import?url=https://api.openstreetmap.org/api/0.6";

  constructor(private appService: AppService) {
  }

  edit(): void {

    this.ready$.next(false);

    const chunkSize = 50;

    const nodeBatches = Range(0, this.page.nodeRefs.count(), chunkSize)
      .map(chunkStart => this.page.nodeRefs.slice(chunkStart, chunkStart + chunkSize))
      .toList();

    const nodeEdits: List<Observable<Object>> = nodeBatches.map(nodeRefs => {
      const nodeIdString = nodeRefs.map(n => n.id).join(",");
      const url = `${this.apiUrl}/nodes?nodes=${nodeIdString}`;
      return this.appService.edit(url).pipe(
        tap(() => this.updateProgress("nodes")),
        delay(500)
      );
    });

    const routeEdits = this.page.routeRefs.take(5).map(routeRef => {
      const url = `${this.apiUrl}/relation/${routeRef.id}/full`;
      return this.appService.edit(url).pipe(
        tap(() => this.updateProgress(`Route ${routeRef.name}`)),
        delay(500)
      );
    });

    let allEdits = nodeEdits.concat(routeEdits);

    if (!this.page.nodeRefs.isEmpty()) {
      const zoomUrl = this.josmUrl + `zoom?left=${this.page.bounds.minLon}&right=${this.page.bounds.maxLon}&top=${this.page.bounds.maxLat}&bottom=${this.page.bounds.minLat}`;
      const setBounds = this.appService.edit(zoomUrl).pipe(
        tap(() => this.updateProgress(`Zoom`))
      );
      allEdits = allEdits.concat(List([setBounds]));
    }

    this.showProgress$.next(true);
    this.progressSteps = allEdits.size;

    concat(...allEdits.toArray()).subscribe(
      result => {
        console.log(["RESULT", result]);
      },
      err => {
        console.log(["ERROR", err]);
      },
      () => {
        this.showProgress$.next(false);
        this.progress$.next(0);
        this.action$.next("");
        this.progressCount = 0;
        this.progressSteps = 0;
        this.ready$.next(true);
      }
    );
  }

  private updateProgress(action: string) {
    console.log("update progress " + this.progressCount);
    this.action$.next(action);
    this.progressCount = this.progressCount + 1;
    let progress = 0;
    if (this.progressSteps > 0) {
      progress = Math.round(100 * this.progressCount / this.progressSteps);
    }
    this.progress$.next(progress);
  }
}
