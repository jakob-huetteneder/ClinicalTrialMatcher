import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';

@Component({
  selector: 'app-delete-popup',
  templateUrl: './delete-popup.component.html',
  styleUrls: ['./delete-popup.component.scss'],
})
export class DeletePopupComponent {
  @ViewChild('myForm') dialog;
  @Input() prompt: string;
  @Output() delete = new EventEmitter<any>();

  deleteEntry(){
    this.closeForm();
    this.delete.emit();
  }

  openForm() {
    this.dialog.nativeElement.style.display = 'flex';
    document.body.style.overflow = 'hidden';
  }

  closeForm() {
    this.dialog.nativeElement.style.display = 'none';
    document.body.style.overflow = 'visible';
  }

}
