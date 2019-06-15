import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from "@angular/core";
import {TranslationUnit} from "./domain/translation-unit";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: "kpn-translation-unit",
  template: `

    <div *ngIf="translationUnit">
      id: {{translationUnit.id}},
      state: {{translationUnit.state}}
    </div>
    <div *ngIf="!translationUnit">
      Select translation
    </div>

    <form [formGroup]="form">
      <div class="text-areas">
        <textarea readonly formControlName="source"></textarea>
        <textarea formControlName="target" (keyup.control.enter)="onSave()"></textarea>
      </div>
      <button mat-stroked-button (click)="onCancel()" [disabled]="form.pristine">Cancel</button>
      <button mat-stroked-button (click)="onSave()" [disabled]="form.pristine">Save &amp; Next</button>
    </form>

    <mat-checkbox [checked]="showLocations" (change)="toggleShowLocations()">Show source code usage locations ({{translationUnit?.locations.size}})</mat-checkbox>
    <div *ngIf="showLocations">
      <div *ngFor="let location of translationUnit?.locations">
        <kpn-translation-location [location]="location"></kpn-translation-location>
      </div>
    </div>

  `,
  styles: [`

    mat-table {
      width: 100%;
    }

    .text-areas {
      display: flex;
    }

    .text-areas > textarea {
      margin: 8px;
      padding: 4px;
      flex-grow: 1;
      font-size: 18px;
    }
  `]
})
export class TranslationUnitComponent implements OnChanges {

  @Input() translationUnit: TranslationUnit;
  @Output() edited = new EventEmitter<TranslationUnit>();

  readonly form: FormGroup;
  readonly source = new FormControl();
  readonly target = new FormControl();

  showLocations = false;

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["translationUnit"]) {
      if (this.translationUnit) {
        this.form.reset({
          source: this.translationUnit.source,
          target: this.translationUnit.target
        });
      } else {
        this.form.reset();
      }
    }
  }

  onCancel(): void {
    this.target.setValue(this.translationUnit.target);
  }

  onSave(): void {
    if (this.translationUnit) {
      const newTranslationUnit = new TranslationUnit(
        this.translationUnit.id,
        this.translationUnit.source,
        this.target.value,
        null,
        this.translationUnit.locations,
        true
      );
      this.edited.emit(newTranslationUnit);
    }
  }

  toggleShowLocations(): void {
    this.showLocations = !this.showLocations;
  }

  private buildForm(): FormGroup {
    return this.fb.group(
      {
        source: this.source,
        target: this.target
      });
  }

}
