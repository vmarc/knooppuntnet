import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';
import { LocationFactsPageService } from './location-facts-page.service';

@Component({
  selector: 'kpn-location-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@location-facts.title"
    >
    </kpn-location-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="service.response | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-facts
          [locationFacts]="response.result.locationFacts"
        ></kpn-location-facts>
      </kpn-location-response>
    </div>
  `,
  providers: [LocationFactsPageService],
})
export class LocationFactsPageComponent implements OnInit {
  constructor(
    public service: LocationFactsPageService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params
      .pipe(first())
      .subscribe((params) => this.service.params(params));
  }
}
