import {Component, OnInit, ViewChild} from '@angular/core';
import {TranslationsService} from "./translations.service";
import {MatPaginator, MatSort, MatTableDataSource} from "@angular/material";
import {TranslationUnit} from "./translation-unit";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {debounceTime} from 'rxjs/operators';

@Component({
  selector: 'translations',
  templateUrl: './translations.component.html',
  styleUrls: ['./translations.component.scss']
})
export class TranslationsComponent implements OnInit {

  translationUnits: TranslationUnit[] = [];

  displayedColumns: string[] = ['id', 'source', 'target'];
  dataSource: MatTableDataSource<TranslationUnit>;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  readonly form: FormGroup;
  readonly filter = new FormControl();


  constructor(private fb: FormBuilder,
              private translationsService: TranslationsService) {
    this.form = this.buildForm();
    this.filter.valueChanges.pipe(debounceTime(500)).subscribe(() => this.onFilterChanged());
  }

  ngOnInit() {
    this.translationsService.translationUnits().subscribe(translationUnits => {
      this.translationUnits = translationUnits;
      this.dataSource = new MatTableDataSource(translationUnits);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    });
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
