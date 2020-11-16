import {Input} from '@angular/core';
import {EventEmitter} from '@angular/core';
import {Output} from '@angular/core';
import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PageWidth} from '../../components/shared/page-width';
import {PageWidthService} from '../../components/shared/page-width.service';

@Component({
  selector: 'kpn-plan-action-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      mat-stroked-button
      *ngIf="showButtonText$ | async"
      (click)="action.emit()"
      [disabled]="!enabled"
      [title]="title">
      <mat-icon [svgIcon]="icon"></mat-icon>
      <span class="button-text">{{text}}</span>
    </button>

    <button
      mat-icon-button
      *ngIf="showButtonIcon$ | async"
      (click)="action.emit()"
      [disabled]="!enabled"
      [title]="title">
      <mat-icon [svgIcon]="icon"></mat-icon>
    </button>
  `,
  styles: [`
    .button-text {
      padding-left: 10px;
    }

    button {
      margin-right: 10px;
    }
  `]
})
export class PlanActionButtonComponent implements OnInit {

  @Input() enabled = false;
  @Input() icon = '';
  @Input() text = '';
  @Input() title = '';
  @Output() action = new EventEmitter<any>();

  showButtonText$: Observable<boolean>;
  showButtonIcon$: Observable<boolean>;

  constructor(private pageWidthService: PageWidthService) {
  }

  ngOnInit(): void {
    this.showButtonText$ = this.pageWidthService.current$.pipe(map(pageWidth => pageWidth === PageWidth.veryLarge || pageWidth === PageWidth.large));
    this.showButtonIcon$ = this.showButtonText$.pipe(map(enabled => !enabled));
  }
}
