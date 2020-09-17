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

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [situationOnEnabled]="false" [response]="response">
        <kpn-location-edit [page]="response.result"></kpn-location-edit>
      </kpn-location-response>
    </div>
  `,
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

