import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RouteType } from '@api/common';
import { IndicatorComponent } from '@app/shared/components/indicator/indicator.component';
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
    />
  `,
  imports: [IndicatorComponent],
})
export class RouteAccessibleIndicatorComponent implements OnInit {
  accessible = input.required<boolean>();
  routeType = input.required<RouteType>();

  private readonly dialog = inject(MatDialog);
  color: string;

  ngOnInit(): void {
    this.color = this.determineColor();
  }

  onOpenDialog() {
    const data = new RouteAccessibleData(this.routeType(), this.accessible(), this.color);
    this.dialog.open(RouteAccessibleIndicatorDialogComponent, {
      data,
      autoFocus: false,
      maxWidth: 600,
    });
  }

  private determineColor() {
    let color = 'gray';
    if ('horse-riding' === this.routeType() || 'inline-skating' === this.routeType()) {
      color = 'gray';
    } else if (
      'cycling' === this.routeType() ||
      'hiking' === this.routeType() ||
      'motorboat' === this.routeType() ||
      'canoe' === this.routeType()
    ) {
      color = this.accessible() ? 'green' : 'red';
    }
    return color;
  }
}
