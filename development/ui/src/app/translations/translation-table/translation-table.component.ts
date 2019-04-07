import {Component, DoCheck, Input, IterableDiffer, IterableDiffers, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MatPaginator, MatSort, MatTableDataSource} from "@angular/material";
import {debounceTime} from 'rxjs/operators';
import {TranslationUnit} from "../domain/translation-unit";

@Component({
  selector: 'translation-table',
  template: `
    <form [formGroup]="form">
      Filter:
      <mat-form-field>
        <input matInput formControlName="filter">
      </mat-form-field>
    </form>

    <mat-divider></mat-divider>
    <mat-paginator [pageSizeOptions]="[5, 10, 20, 50]" [length]="translationUnits?.length" showFirstLastButtons></mat-paginator>
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
      <mat-row *matRowDef="let row; columns: displayedColumns;"></mat-row>
    </mat-table>
  `,
  styles: [`
    mat-table {
      width: 100%;
    }
  `]
})
export class TranslationTableComponent implements DoCheck {

  @Input() translationUnits: Array<TranslationUnit> = [];

  displayedColumns: Array<string> = ['state', 'id', 'source', 'target', 'sourceFile'];
  dataSource: MatTableDataSource<TranslationUnit>;
  readonly differ: IterableDiffer<TranslationUnit>;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  readonly form: FormGroup;
  readonly filter = new FormControl();

  constructor(private fb: FormBuilder,
              private differs: IterableDiffers,
  ) {
    this.differ = differs.find([]).create(null);
    this.form = this.buildForm();
    this.filter.valueChanges.pipe(debounceTime(500)).subscribe(() => this.onFilterChanged());
  }

  ngDoCheck() {
    if (this.differ.diff(this.translationUnits)) {
      this.dataSource = new MatTableDataSource(this.translationUnits);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    }
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
