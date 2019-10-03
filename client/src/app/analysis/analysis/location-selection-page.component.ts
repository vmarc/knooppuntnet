import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {flatMap, map} from "rxjs/operators";
import {AppService} from "../../app.service";
import {Country} from "../../kpn/shared/country";
import {NetworkType} from "../../kpn/shared/network-type";
import {Subscriptions} from "../../util/Subscriptions";

@Component({
  selector: "kpn-location-selection-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <a routerLink="{{networkTypeLink()}}">
        <kpn-network-type-name [networkType]="networkType"></kpn-network-type-name>
      </a> >
      <kpn-country-name [country]="country"></kpn-country-name>
    </div>

    <kpn-page-header [pageTitle]="'TODO'" subject="network-page">
      <kpn-network-type-name [networkType]="networkType"></kpn-network-type-name>
      <span i18n="@@subset.in">in</span>
      <kpn-country-name [country]="country"></kpn-country-name>
    </kpn-page-header>

    <div class="option-title">
      <mat-icon svgIcon="dot" class="dot-icon"></mat-icon>
      <a [routerLink]="'/analysis/' + networkType.name + '/' + country.domain + '/networks'">Networks</a> <span class="node-count">(123)</span>
    </div>

    <div class="option-title">
      <mat-icon svgIcon="dot" class="dot-icon"></mat-icon>
      <span>Location</span>
    </div>

    <div class="option-body">

      <div class="sub-option-title">
        <mat-icon svgIcon="dot" class="dot-icon"></mat-icon>
        <span>Enter name:</span>
      </div>
      <div class="sub-option-body">
        <kpn-location-selector [country]="country" (selection)="selected($event)"></kpn-location-selector>
      </div>

      <div class="sub-option-title">
        <mat-icon svgIcon="dot" class="dot-icon"></mat-icon>
        <span>Select from tree:</span>
      </div>
      <div class="sub-option-body">
        <kpn-location-tree [country]="country" (selection)="selected($event)"></kpn-location-tree>
      </div>
    </div>
  `,
  styles: [`
    /deep/ .dot-icon > svg {
      width: 6px;
      height: 6px;
      vertical-align: middle;
      color: lightgrey;
    }

    .node-count {
      padding-left: 20px;
      font-size: 16px;
      color: gray;
    }

    .location {
      padding-top: 50px;
      font-size: 25px;
    }

    .option-title {
      padding-top: 30px;
    }

    .option-body {
      padding-top: 10px;
      padding-left: 30px;
    }

    .sub-option-title {
      padding-top: 20px;
    }

    .sub-option-body {
      padding-top: 10px;
      padding-bottom: 40px;
      padding-left: 30px;
    }

  `]
})
export class LocationSelectionPageComponent implements OnInit {

  networkType: NetworkType;
  country: Country;

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private router: Router) {
  }

  selected(locationName: string): void {
    const url = `/analysis/${this.networkType.name}/${this.country.domain}/${locationName}/nodes`;
    this.router.navigateByUrl(url);
  }

  ngOnInit() {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => {
          this.networkType = NetworkType.withName(params["networkType"]);
          this.country = new Country(params["country"]);
          return params["networkType"];
        }),
        flatMap(networkType => this.appService.location(networkType))
      ).subscribe(response => { /* process result */
      })
    );
  }

  networkTypeLink(): string {
    return `/analysis/${this.networkType.name}`;
  }

}
