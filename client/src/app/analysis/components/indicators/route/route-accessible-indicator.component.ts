import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkType } from '@api/custom/network-type';
import { RouteAccessibleData } from './route-accessible-data';
import { RouteAccessibleIndicatorDialogComponent } from './route-accessible-indicator-dialog.component';

@Component({
  selector: 'kpn-route-accessible-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="A"
      i18n-letter="@@route-accessible-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    >
    </kpn-indicator>
  `,
})
export class RouteAccessibleIndicatorComponent implements OnInit {
  @Input() accessible: boolean;
  @Input() networkType: NetworkType;
  color: string;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.color = this.determineColor();
  }

  onOpenDialog() {
    const data = new RouteAccessibleData(
      this.networkType,
      this.accessible,
      this.color
    );
    this.dialog.open(RouteAccessibleIndicatorDialogComponent, {
      data,
      autoFocus: false,
      maxWidth: 600,
    });
  }

  private determineColor() {
    let color = 'gray';
    if (
      'horse-riding' === this.networkType ||
      'inline-skating' === this.networkType
    ) {
      color = 'gray';
    } else if (
      'cycling' === this.networkType ||
      'hiking' === this.networkType ||
      'motorboat' === this.networkType ||
      'canoe' === this.networkType
    ) {
      color = this.accessible ? 'green' : 'red';
    }
    return color;
  }
}
