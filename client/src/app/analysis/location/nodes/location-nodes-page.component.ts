import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';
import { LocationNodesPageService } from './location-nodes-page.service';

@Component({
  selector: 'kpn-location-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="nodes"
      pageTitle="Nodes"
      i18n-pageTitle="@@location-nodes.title"
    >
    </kpn-location-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="service.response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-nodes [page]="response.result"></kpn-location-nodes>
      </kpn-location-response>
    </div>
  `,
  providers: [LocationNodesPageService],
})
export class LocationNodesPageComponent implements OnInit {
  constructor(
    public service: LocationNodesPageService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params
      .pipe(first())
      .subscribe((params) => this.service.params(params));
  }
}
