import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { TagDetailType } from '@api/common/diff';

@Component({
  selector: 'kpn-tag-diff-action',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @switch (action.name) {
      @case ('Add') {
        <mat-icon svgIcon="add" />
      }
      @case ('Update') {
        <mat-icon svgIcon="update" />
      }
      @case ('Delete') {
        <mat-icon svgIcon="remove" />
      }
    }
  `,
  standalone: true,
  imports: [MatIconModule],
})
export class TagDiffActionComponent {
  @Input() action: TagDetailType;
}
