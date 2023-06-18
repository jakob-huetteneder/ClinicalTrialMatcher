import {Component, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {FaqService} from '../../services/faq.service';

@Component({
  selector: 'app-interactivefaq',
  templateUrl: './interactivefaq.component.html',
  styleUrls: ['./interactivefaq.component.scss']
})
export class InteractivefaqComponent implements OnInit {
  messageList: string[] = [];
  chatStatus = false;

  constructor(
    private faqService: FaqService,
    private notification: ToastrService,
  ) {
  }

  ngOnInit() {

  }

  isChatOpen(): boolean {
    return this.chatStatus;
  }

  sendFaq(keyword): void {
    this.faqService.getFaqAwnser(keyword).subscribe({
      next: updatedUser => {

        // remove user from editedUsers
        this.messageList = this.editedUsers.filter(editedUser => editedUser.id !== user.id);
        this.notification.info('Successfully updated user ' + updatedUser.email);
      },
      error: error => {
        if (error.status === 409) {
          this.notification.error('User with email ' + user.email + ' already exists');
        } else if (error.status === 422) {
          let listOfValidationErrors = '';
          error.error.errors.forEach((validationError: string) => {
            if (listOfValidationErrors !== '') {
              listOfValidationErrors += ', ';
            }
            listOfValidationErrors += validationError;
          });
          this.notification.error(listOfValidationErrors, 'Invalid values');
        } else {
          this.notification.error(error.error.message, 'Error updating user');
        }
        // reset user to old values
        this.resetUser(user.id);
      }
    });
  }
}
