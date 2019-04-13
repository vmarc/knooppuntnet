import {Component} from "@angular/core";

@Component({
  selector: "kpn-map-poi-config",
  template: `
    <kpn-poi-names></kpn-poi-names>
    <kpn-poi-group-hiking-biking></kpn-poi-group-hiking-biking>
    <kpn-poi-group-landmarks></kpn-poi-group-landmarks>
    <kpn-poi-group-restaurants></kpn-poi-group-restaurants>
    <kpn-poi-group-places-to-stay></kpn-poi-group-places-to-stay>
    <kpn-poi-group-tourism></kpn-poi-group-tourism>
    <kpn-poi-group-amenity></kpn-poi-group-amenity>
    <kpn-poi-group-shops></kpn-poi-group-shops>
    <kpn-poi-group-foodshops></kpn-poi-group-foodshops>
    <kpn-poi-group-sports></kpn-poi-group-sports>
  `,
  styles: [`

    /deep/ .subset-title {
      margin-top: 40px;
      margin-bottom: 20px;
      font-weight: 600;
    }

    /deep/ .col-icon {
      display: inline-block;
      position: relative;
      width: 35px;
    }

    /deep/ .col-spacer {
      display: inline-block;
      position: relative;
      width: 40px;
    }

    /deep/ .col-name {
      display: inline-block;
      position: relative;
      width: 130px;
      line-height: 37px;
      vertical-align: top;
    }

    /deep/ .col-level-0 {
      display: inline-block;
      position: relative;
      width: 40px;
      vertical-align: top;
    }

    /deep/ .col-level-11 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

    /deep/ .col-level-12 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

    /deep/ .col-level-13 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

    /deep/ .col-level-14 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

    /deep/ .col-level-15 {
      display: inline-block;
      position: relative;
      width: 30px;
      vertical-align: top;
    }

  `]
})
export class MapPoiConfigComponent {
}
