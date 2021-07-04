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
      maxWidth: 600,
    });
  }

  private determineColor() {
    let color = 'gray';
    if (
      NetworkType.horseRiding.name === this.networkType.name ||
      NetworkType.inlineSkating.name === this.networkType.name
    ) {
      color = 'gray';
    } else if (
      NetworkType.cycling.name === this.networkType.name ||
      NetworkType.hiking.name === this.networkType.name ||
      NetworkType.motorboat.name === this.networkType.name ||
      NetworkType.canoe.name === this.networkType.name
    ) {
      color = this.accessible ? 'green' : 'red';
    }
    return color;
  }
}
