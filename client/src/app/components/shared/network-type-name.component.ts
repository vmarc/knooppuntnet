import {Component, Input} from "@angular/core";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {I18nService} from "../../i18n/i18n.service";
import {Util} from "./util";

@Component({
  selector: "kpn-network-type-name",
  template: `
    {{networkTypeName()}}
  `
})
export class NetworkTypeNameComponent {

  @Input() networkType: NetworkType;

  constructor(private i18nService: I18nService) {
  }

  networkTypeName(): string {
    return this.i18nService.translation("@@network-type." + Util.safeGet(() => this.networkType.name));
  }

}
