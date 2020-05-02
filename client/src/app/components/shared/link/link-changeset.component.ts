import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-changeset",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/changeset/' + changeSetId + '/' + replicationNumber">{{changeSetId}}</a>
  `
})
export class LinkChangesetComponent {
  @Input() changeSetId: number;
  @Input() replicationNumber: number;
}
