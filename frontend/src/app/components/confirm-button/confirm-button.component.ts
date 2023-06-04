import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {ConfirmDialogComponent, ConfirmDialogResult} from './confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-confirm-button',
  templateUrl: './confirm-button.component.html',
  styleUrls: ['./confirm-button.component.scss']
})
export class ConfirmButtonComponent {

  @Output()
  confirm = new EventEmitter<any>();

  @Output()
  cancel = new EventEmitter<any>();

  @Input()
  prompt = 'Are you sure?';

  @Input()
  details = 'This action cannot be undone.';

  @Input()
  confirmText = 'Yes';

  @Input()
  cancelText = 'No';

  constructor(private dialog: MatDialog) {
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.hasBackdrop = true;

    dialogConfig.data = {
      prompt: this.prompt,
      details: this.details,
      confirmText: this.confirmText,
      cancelText: this.cancelText
    };

    const dialogRef = this.dialog.open(ConfirmDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(result => {
      if (result === ConfirmDialogResult.confirmed) {
        this.confirm.emit();
      } else {
        this.cancel.emit();
      }
    });
  }
}
