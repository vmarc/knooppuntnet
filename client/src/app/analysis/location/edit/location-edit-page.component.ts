import {ChangeDetectionStrategy, Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {first} from "rxjs/operators";
import {LocationEditPage} from "../../../kpn/api/common/location/location-edit-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationEditPageService} from "./location-edit-page.service";

@Component({
  selector: "kpn-location-edit-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="edit"
      pageTitle="Load in editor"
      i18n-pageTitle="@@location-edit.title">
    </kpn-location-page-header>

    <div *ngIf="response$ | async as response; else analyzing" class="kpn-spacer-above">
      <kpn-location-response [situationOnEnabled]="false" [response]="response">
        <p *ngIf="response.result.tooManyNodes" class="too-many-nodes" i18n="@@location-edit.too-many-nodes.1">
          This location contains more than the maximum number of nodes ({{response.result.maxNodes}})
          that can be loaded in the editor in one go. This limitation is to avoid overloading the
          OpenStreetMap api while loading the node and route details from JOSM.
        </p>
        <p *ngIf="response.result.tooManyNodes" class="too-many-nodes" i18n="@@location-edit.too-many-nodes.2">
          Please select a location with less nodes.
        </p>
        <kpn-location-edit
          *ngIf="!response.result.tooManyNodes"
          [page]="response.result">
        </kpn-location-edit>
      </kpn-location-response>
    </div>
    <ng-template #analyzing>
      <p class="analyzing" i18n="@@location-edit.analyzing">
        Analyzing location nodes and routes, please wait...
      </p>
    </ng-template>
  `,
  styles: [`
    .too-many-nodes {
      max-width: 40em;
      font-style: italic;
    }

    .analyzing {
      font-style: italic;
    }
  `],
  providers: [
    LocationEditPageService
  ]
})
export class LocationEditPageComponent implements OnInit {

  readonly response$: Observable<ApiResponse<LocationEditPage>>;

  constructor(private service: LocationEditPageService,
              private activatedRoute: ActivatedRoute) {
    this.response$ = service.response$;
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(first()).subscribe(params => this.service.params(params));
  }
}

