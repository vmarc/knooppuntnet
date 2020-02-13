import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-changeset",
  template: `
    <a [routerLink]="'/analysis/changeset/' + changeSetId + '/' + replicationNumber">{{changeSetId}}</a>
  `
})
export class LinkChangesetComponent {
  @Input() changeSetId: number;
  @Input() replicationNumber: number;
}
