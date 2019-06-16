import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild} from "@angular/core";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MatPaginator, MatSort, MatTableDataSource} from "@angular/material";
import {debounceTime} from "rxjs/operators";
import {Subscriptions} from "../util/Subscriptions";
import {TranslationUnit} from "./domain/translation-unit";
import {List} from "immutable";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: "kpn-translation-table",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form">
      Filter:
      <mat-form-field>
        <input matInput formControlName="filter">
      </mat-form-field>
      <mat-checkbox [checked]="showTranslated" (change)="toggleShowTranslated()">Show translated</mat-checkbox>
    </form>

    <mat-divider></mat-divider>
    <mat-paginator [pageSizeOptions]="[5, 10, 20, 50, 200, 1000]" [length]="translationUnits?.size" showFirstLastButtons></mat-paginator>
    <mat-divider></mat-divider>

    <mat-table matSort [dataSource]="dataSource">

      <ng-container matColumnDef="state">
        <mat-header-cell *matHeaderCellDef mat-sort-header>State</mat-header-cell>
        <mat-cell *matCellDef="let row"> {{row.state}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="id">
        <mat-header-cell *matHeaderCellDef mat-sort-header>Id</mat-header-cell>
        <mat-cell *matCellDef="let row"> {{row.id}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="source">
        <mat-header-cell *matHeaderCellDef mat-sort-header>Source</mat-header-cell>
        <mat-cell *matCellDef="let row"> {{row.source}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="target">
        <mat-header-cell *matHeaderCellDef mat-sort-header>Target</mat-header-cell>
        <mat-cell *matCellDef="let row"> {{row.target}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="sourceFile">
        <mat-header-cell *matHeaderCellDef mat-sort-header>Source file</mat-header-cell>
        <mat-cell *matCellDef="let row"> {{row.sourceFile}}</mat-cell>
      </ng-container>

      <mat-header-row *matHeaderRowDef="displayedColumns"></mat-header-row>
      <mat-row
        *matRowDef="let row; columns: displayedColumns;"
        [ngClass]="{'selected-row': selection.isSelected(row), 'selectable': true}"
        (click)="selection.toggle(row)">
      </mat-row>

    </mat-table>
  `,
  styles: [`
    
    mat-table {
      width: 100%;
    }

    .selected-row {
      background-color: lightgrey;
    }
  `]
})
export class TranslationTableComponent implements OnInit, OnChanges, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  @Input() translationUnits: List<TranslationUnit>;
  @Output() selected = new EventEmitter<TranslationUnit>();

  showTranslated = false;

  displayedColumns: Array<string> = ["state", "id", "source", "target", "sourceFile"];
  dataSource: MatTableDataSource<TranslationUnit>;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  readonly form: FormGroup;
  readonly filter = new FormControl();

  readonly selection = new SelectionModel<TranslationUnit>(false, [], true);

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
    this.subscriptions.add(this.filter.valueChanges.pipe(debounceTime(500)).subscribe(() => this.onFilterChanged()));
    this.selection.changed.subscribe(change => {
      const translationUnit = this.selection.selected.length > 0 ? this.selection.selected[0] : null;
      this.selected.emit(translationUnit);
    });
  }

  ngOnInit(): void {
    this.updateDataSource();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["translationUnits"]) {
      this.updateDataSource();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  toggleShowTranslated() {
    this.showTranslated = !this.showTranslated;
    this.updateDataSource();
  }

  private updateDataSource(): void {
    const units = this.filteredTranslationUnits();
    this.dataSource = new MatTableDataSource(units);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    if (units.length > 0) {
      this.selection.select(units[0]);
    }
  }

  private filteredTranslationUnits(): Array<TranslationUnit> {
    return this.translationUnits.filter(tu => this.showTranslated || tu.state === "new").toArray();
  }

  private onFilterChanged() {
    this.dataSource.filter = this.filter.value;
  }

  private buildForm(): FormGroup {
    return this.fb.group(
      {
        filter: this.filter
      });
  }

}
