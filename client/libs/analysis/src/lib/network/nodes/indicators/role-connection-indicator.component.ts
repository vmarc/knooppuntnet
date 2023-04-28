import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkNodeRow } from '@api/common/network';
import { RoleConnectionIndicatorDialogComponent } from './role-connection-indicator-dialog.component';

@Component({
  selector: 'kpn-role-connection-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="C"
      i18n-letter="@@role-connection-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    />
  `,
})
export class RoleConnectionIndicatorComponent implements OnInit {
  @Input() node: NetworkNodeRow;
  color: string;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.color = this.node.detail.roleConnection ? 'blue' : 'gray';
  }

  onOpenDialog() {
    this.dialog.open(RoleConnectionIndicatorDialogComponent, {
      data: this.color,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
