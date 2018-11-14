import {Component, Input} from '@angular/core';
import {Subset} from "../../kpn/shared/subset";
import {Country} from "../../kpn/shared/country";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: 'kpn-sidenav-subsets',
  template: `
    <kpn-sidenav-menu title="Analysis">
      <a *ngFor="let s of allSubsets" mat-list-item routerLink="{{subsetUrl(s)}}">{{subsetTitle(s)}}</a>
    </kpn-sidenav-menu>
  `
})
export class SidenavSubsetsComponent {

  @Input() subset: Subset;

  allSubsets = [
    new Subset(new Country("nl"), new NetworkType("rcn")),
    new Subset(new Country("be"), new NetworkType("rcn")),
    new Subset(new Country("de"), new NetworkType("rcn")),
    new Subset(new Country("nl"), new NetworkType("rwn")),
    new Subset(new Country("be"), new NetworkType("rwn")),
    new Subset(new Country("nl"), new NetworkType("rhn")),
    new Subset(new Country("nl"), new NetworkType("rmn")),
    new Subset(new Country("nl"), new NetworkType("rpn")),
    new Subset(new Country("nl"), new NetworkType("rin")),
  ];

  subsetUrl(s: Subset): string {
    return '/analysis/networks/' + s.country.domain + '/' + s.networkType.name;
  }

  subsetTitle(s: Subset): string {
    return this.networkTypeName(s.networkType) + " in " + this.countryName(s.country);
  }

  private countryName(country: Country): string {
    let countryName = "";
    switch (country.domain) {
      case "nl":
        countryName = "The Netherlands";
        break;
      case "be":
        countryName = "Belgium";
        break;
      case "de":
        countryName = "Germany";
        break;
    }
    return countryName;
  }

  private networkTypeName(networkType: NetworkType): string {
    let networkTypeName = "";
    switch (networkType.name) {
      case "rcn":
        networkTypeName = "Cycling";
        break;
      case "rwn":
        networkTypeName = "Hiking";
        break;
      case "rhn":
        networkTypeName = "Horse";
        break;
      case "rmn":
        networkTypeName = "Motorboat";
        break;
      case "rpn":
        networkTypeName = "Canoe";
        break;
      case "rin":
        networkTypeName = "Inline skating";
        break;
    }
    return networkTypeName;
  }

}
