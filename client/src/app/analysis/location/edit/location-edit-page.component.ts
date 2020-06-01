import {ChangeDetectionStrategy, Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {LocationEditPageService} from "./location-edit-page.service";

@Component({
  selector: "kpn-location-edit-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="edit"
      pageTitle="Edit"
      i18n-pageTitle="@@location-edit.title">
    </kpn-location-page-header>

    <div *ngIf="service.response$ | async as response" class="kpn-spacer-above">
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

  constructor(public service: LocationEditPageService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(first()).subscribe(params => this.service.params(params));
  }
}

