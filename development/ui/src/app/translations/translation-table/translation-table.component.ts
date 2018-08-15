import {Component, DoCheck, Input, IterableDiffer, IterableDiffers, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from "@angular/material";
import {TranslationUnit} from "../domain/translation-unit";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {debounceTime} from 'rxjs/operators';

@Component({
  selector: 'translation-table',
  templateUrl: './translation-table.component.html',
  styleUrls: ['./translation-table.component.scss']
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
