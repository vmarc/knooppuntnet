import {Component} from '@angular/core';
import {OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {ValidatorFn} from '@angular/forms';
import {MatTableDataSource} from '@angular/material/table';
import {Util} from '../shared/util';
import {MessageService} from '../shared/message.service';

class Row {
  constructor(readonly name: string) {
  }
}

@Component({
  selector: 'app-page3',
  template: `
    <h1>Page 3 - form table validation</h1>

    <app-menu></app-menu>

    <div class="buttons">
      <button mat-raised-button (click)="add()">Add</button>
      <button mat-raised-button (click)="remove()">Remove</button>
    </div>

    <div>
      <form [formGroup]="form">

        <table mat-table [dataSource]="dataSource">

          <ng-container matColumnDef="name">
            <th mat-header-cell *matHeaderCellDef>Name</th>
            <td mat-cell *matCellDef="let element"> {{element.name}} </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>

        <app-form-errors [form]="form"></app-form-errors>

        <button mat-raised-button type="submit" (click)="submit()" color="primary">Submit</button>

      </form>
    </div>

    <div class="debug">
      <p>
        Debug:
      </p>
      <ul>
        <li>form.valid = {{form.valid}}</li>
      </ul>
      <ul>
        <li>form.errors = {{util.toMessages(form.errors).join(", ")}}</li>
      </ul>
    </div>
  `,
  styles: [`
    .buttons {
      padding-top: 2em;
      padding-bottom: 1em;
    }

    .buttons button {
      margin-right: 1em;
    }

    form button {
      margin-top: 2em;
    }

    .debug {
      padding-top: 4em;
      font-family: monospace;
    }
  `]
})
export class Page3Component implements OnInit {

  readonly displayedColumns: string[] = ['name'];
  readonly dataSource: MatTableDataSource<Row> = new MatTableDataSource();
  readonly form = new FormGroup({}, this.tableValidator(this.dataSource));

  readonly util = Util;

  private id = 1;

  constructor(private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.dataSource.data = [];
    this.add();
    this.add();
    this.add();
  }

  submit(): void {
    if (this.form.valid) {
      this.messageService.say('submit valid form');
    } else {
      this.messageService.warn('cannot submit invalid form');
    }
  }

  add() {
    this.dataSource.data = this.dataSource.data.concat(new Row(`row ${this.id}`));
    this.form.updateValueAndValidity();
    this.id = this.id + 1;
  }

  remove() {
    this.dataSource.data = this.dataSource.data.slice(1);
    this.form.updateValueAndValidity();
  }

  private tableValidator(dataSource: MatTableDataSource<any>): ValidatorFn {
    return () => {
      if (dataSource.data.length === 3) {
        return null;
      }
      return {'Table should contain 3 elements': true};
    };
  }
}
