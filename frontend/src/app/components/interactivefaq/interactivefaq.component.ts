import {Component, OnInit} from '@angular/core';
import {Faq} from '../../dtos/faq';
import {ToastrService} from 'ngx-toastr';
import {FaqService} from '../../services/faq.service';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-interactivefaq',
  templateUrl: './interactivefaq.component.html',
  styleUrls: ['./interactivefaq.component.scss']
})
export class InteractivefaqComponent implements OnInit {
  messageList: Faq[] = [];
  chatStatus = false;
  role = '';

  constructor(
    private faqService: FaqService,
    private notification: ToastrService,
    private authService: AuthService
  ) {
  }

  ngOnInit() {
    this.sendFaq('xintro');
  }

  isChatOpen(): boolean {
    return this.chatStatus;
  }

  resetMessages() {
    this.messageList = [];
    this.sendFaq('xintro');
  }

  sendFaq(keyword): void {
    if (this.authService.isLoggedIn()) {
      this.role = this.authService.getUserRole();
    } else {
      this.role = 'unregistered';
    }
    this.faqService.getFaqAnswer(keyword, this.role).subscribe({
      next: (message: Faq) => {
        this.messageList.push(message);
      },
      error: error => {
        console.log('Something went wrong while loading answers: ' + error.error.message);
        this.notification.error(error.error.message, 'Something went wrong while loading answers');
        console.log(error);
      }
    });
  }
}
