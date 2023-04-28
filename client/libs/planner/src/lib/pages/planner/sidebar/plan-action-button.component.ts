import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { Input } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { PageWidth } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'kpn-plan-action-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      mat-stroked-button
      *ngIf="showButtonText$ | async"
      (click)="action.emit()"
      [disabled]="!enabled"
      [title]="title"
    >
      <mat-icon [svgIcon]="icon" />
      <span class="button-text">{{ text }}</span>
    </button>

    <button
      mat-icon-button
      *ngIf="showButtonIcon$ | async"
      (click)="action.emit()"
      [disabled]="!enabled"
      [title]="title"
    >
      <mat-icon [svgIcon]="icon" />
    </button>
  `,
  styles: [
    `
      .button-text {
        padding-left: 10px;
      }

      button {
        margin-right: 10px;
      }

      button > mat-icon {
        height: 18px;
        line-height: 18px;
      }
    `,
  ],
  standalone: true,
  imports: [NgIf, MatButtonModule, MatIconModule, AsyncPipe],
})
export class PlanActionButtonComponent implements OnInit {
  @Input() enabled = false;
  @Input() icon = '';
  @Input() text = '';
  @Input() title = '';
  @Output() action = new EventEmitter<any>();

  showButtonText$: Observable<boolean>;
  showButtonIcon$: Observable<boolean>;

  constructor(private pageWidthService: PageWidthService) {}

  ngOnInit(): void {
    this.showButtonText$ = this.pageWidthService.current$.pipe(
      map(
        (pageWidth) =>
          pageWidth === PageWidth.veryLarge || pageWidth === PageWidth.large
      )
    );
    this.showButtonIcon$ = this.showButtonText$.pipe(
      map((enabled) => !enabled)
    );
  }
}
