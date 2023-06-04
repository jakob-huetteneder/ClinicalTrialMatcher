import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

export enum ConfirmDialogResult {
  confirmed,
  canceled
}

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent {

  prompt: string;
  details: string;
  confirmText: string;
  cancelText: string;


  constructor(
    private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data
  ) {
    this.prompt = data.prompt;
    this.details = data.details;
    this.confirmText = data.confirmText;
    this.cancelText = data.cancelText;
  }

  confirm() {
    this.dialogRef.close(ConfirmDialogResult.confirmed);
  }

  cancel() {
    this.dialogRef.close(ConfirmDialogResult.canceled);
  }
}
