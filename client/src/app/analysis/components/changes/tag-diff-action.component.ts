import {Component, Input} from "@angular/core";
import {TagDetailType} from "../../../kpn/shared/diff/tag-detail-type";

@Component({
  selector: "kpn-tag-diff-action",
  template: `
    <mat-icon *ngIf="isAdd()" svgIcon="add"></mat-icon>
    <mat-icon *ngIf="isUpdate()" svgIcon="update"></mat-icon>
    <mat-icon *ngIf="isDelete()" svgIcon="remove"></mat-icon>
  `
})
export class TagDiffActionComponent {

  @Input() action: TagDetailType;

  isAdd(): boolean {
    return this.action.name == "Add";
  }

  isUpdate(): boolean {
    return this.action.name == "Update";
  }

  isDelete(): boolean {
    return this.action.name == "Delete";
  }

  isSame(): boolean {
    return this.action.name == "Same";
  }

}
