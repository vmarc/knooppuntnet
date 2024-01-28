import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkNodeRow } from '@api/common/network';
import { IndicatorComponent } from '@app/components/shared/indicator';
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
  standalone: true,
  imports: [IndicatorComponent],
})
export class RoleConnectionIndicatorComponent implements OnInit {
  node = input<NetworkNodeRow | undefined>();

  private readonly dialog = inject(MatDialog);
  color: string;

  ngOnInit(): void {
    this.color = this.node().detail.roleConnection ? 'blue' : 'gray';
  }

  onOpenDialog() {
    this.dialog.open(RoleConnectionIndicatorDialogComponent, {
      data: this.color,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
