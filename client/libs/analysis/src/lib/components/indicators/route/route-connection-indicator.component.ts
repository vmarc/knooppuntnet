import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkRouteRow } from '@api/common/network';
import { IndicatorComponent } from '@app/components/shared/indicator';
import { RouteConnectionIndicatorDialogComponent } from './route-connection-indicator-dialog.component';

@Component({
  selector: 'kpn-route-connection-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="C"
      i18n-letter="@@route-connection-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    />
  `,
  standalone: true,
  imports: [IndicatorComponent],
})
export class RouteConnectionIndicatorComponent implements OnInit {
  @Input() route: NetworkRouteRow;
  color: string;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.color = this.route.roleConnection ? 'blue' : 'gray';
  }

  onOpenDialog() {
    this.dialog.open(RouteConnectionIndicatorDialogComponent, {
      data: this.color,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
