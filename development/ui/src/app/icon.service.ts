declare let require: any;

import {Injectable} from "@angular/core";
import {DomSanitizer} from "@angular/platform-browser";
import {MatIconRegistry} from "@angular/material";

@Injectable()
export class IconService {

  constructor(private iconRegistry: MatIconRegistry,
              private domSanitizer: DomSanitizer) {
    this.registerIcons();
  }

  registerIcons() {

    this.iconRegistry.addSvgIconLiteral("analysis", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/heartbeat.svg")));

    this.iconRegistry.addSvgIconLiteral("planner", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/planner.svg")));


    // networkTypes

    this.iconRegistry.addSvgIconLiteral("rcn", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/man-cycling.svg")));

    this.iconRegistry.addSvgIconLiteral("rwn", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/man-walking-to-right.svg")));

    this.iconRegistry.addSvgIconLiteral("rhn", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/horseshoe-black.svg")));

    this.iconRegistry.addSvgIconLiteral("rmn", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/boat.svg")));

    this.iconRegistry.addSvgIconLiteral("rpn", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/canoe-racing.svg")));

    this.iconRegistry.addSvgIconLiteral("rin", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/roller-skate.svg")));


    // analysis results

    this.iconRegistry.addSvgIconLiteral("happy", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/happy.svg")));

    this.iconRegistry.addSvgIconLiteral("investigate", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/investigate.svg")));


    // menu

    this.iconRegistry.addSvgIconLiteral("menu", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/menu-button.svg")));

    this.iconRegistry.addSvgIconLiteral("help", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/info.svg")));

    this.iconRegistry.addSvgIconLiteral("expand", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/expand-arrow.svg")));

    this.iconRegistry.addSvgIconLiteral("collapse", this.domSanitizer.bypassSecurityTrustHtml(
      require("!svg-inline-loader!src/assets/images/icons/right.svg")));

  }

}
