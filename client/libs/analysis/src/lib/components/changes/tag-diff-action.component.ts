import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { TagDetailType } from '@api/common/diff';

@Component({
  selector: 'kpn-tag-diff-action',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-icon *ngIf="isAdd()" svgIcon="add" />
    <mat-icon *ngIf="isUpdate()" svgIcon="update" />
    <mat-icon *ngIf="isDelete()" svgIcon="remove" />
  `,
  standalone: true,
  imports: [NgIf, MatIconModule],
})
export class TagDiffActionComponent {
  @Input() action: TagDetailType;

  isAdd(): boolean {
    return this.action.name === 'Add';
  }

  isUpdate(): boolean {
    return this.action.name === 'Update';
  }

  isDelete(): boolean {
    return this.action.name === 'Delete';
  }

  isSame(): boolean {
    return this.action.name === 'Same';
  }
}
