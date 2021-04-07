import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';
import { LocationChangesPageService } from './location-changes-page.service';

@Component({
  selector: 'kpn-location-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@location-changes.title"
    >
    </kpn-location-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="service.response | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-changes></kpn-location-changes>
      </kpn-location-response>
    </div>
  `,
  providers: [LocationChangesPageService],
})
export class LocationChangesPageComponent implements OnInit {
  constructor(
    public service: LocationChangesPageService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params
      .pipe(first())
      .subscribe((params) => this.service.params(params));
  }
}
