import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class MessageService {
  constructor(private snackBar: MatSnackBar) {}

  say(message: string): void {
    this.snackBar.open(message, 'close', {
      panelClass: ['mat-toolbar', 'mat-primary'],
    });
  }

  warn(message: string): void {
    this.snackBar.open(message, 'close', {
      panelClass: ['mat-toolbar', 'mat-warn'],
    });
  }
}
