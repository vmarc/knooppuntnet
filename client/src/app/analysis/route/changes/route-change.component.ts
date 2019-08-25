import {Component, Input} from "@angular/core";
import {RouteChangeInfo} from "../../../kpn/shared/route/route-change-info";

@Component({
  selector: "kpn-route-change",
  template: `

    <kpn-change-header
      [changeKey]="routeChangeInfo.changeKey"
      [happy]="routeChangeInfo.happy"
      [investigate]="routeChangeInfo.investigate"
      [comment]="routeChangeInfo.comment">
    </kpn-change-header>

    <kpn-change-set-tags [changeSetTags]="routeChangeInfo.changeSetInfo.tags"></kpn-change-set-tags>

    <div class="kpn-detail">
      Version {{routeChangeInfo.version}} <span *ngIf="isVersionUnchanged()">(Unchanged)</span>
    </div>

    <kpn-route-change-detail [routeChangeInfo]="routeChangeInfo"></kpn-route-change-detail>

    <pre>
      {{contents()}}
    </pre>
  `
})
export class RouteChangeComponent {

  @Input() routeChangeInfo: RouteChangeInfo;

  contents(): string {
    return JSON.stringify(this.routeChangeInfo, null, 2);
  }

  isVersionUnchanged(): boolean {
    const before = this.routeChangeInfo.before ? this.routeChangeInfo.before.version : null;
    const after = this.routeChangeInfo.after ? this.routeChangeInfo.after.version : null;
    return before && after && before == after;
  }

}
