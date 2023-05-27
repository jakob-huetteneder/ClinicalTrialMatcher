import {Component, OnInit} from '@angular/core';
import {Treats, TreatsStatus} from '../../../dtos/patient';
import {TreatsService} from '../../../services/treats.service';

@Component({
  selector: 'app-doctor-requests',
  templateUrl: './view-requests.component.html',
  styleUrls: ['./view-requests.component.scss']
})
export class ViewRequestsComponent implements OnInit {

  requests: Treats[] = [];

  constructor(
    private treatsService: TreatsService,
  ) { }

  ngOnInit(): void {
    this.loadTreats();
  }

  accept(request: Treats) {
    this.respondToRequest(request, true);
  }

  decline(request: Treats) {
    this.respondToRequest(request, false);
  }

  private loadTreats() {
    this.treatsService.getAllRequests().subscribe({
      next: (requests: Treats[]) => {
        this.requests = requests.filter(request => request.status === TreatsStatus.requested);
        console.log(requests);
      },
      error: error => {
        console.log('Something went wrong while loading requests: ' + error.error.message);
      }
    });
  }

  private respondToRequest(request: Treats, accept: boolean) {
    this.treatsService.respondToRequest(request.doctor.id, accept).subscribe({
      next: () => {
        this.loadTreats();
      },
      error: error => {
        console.log('Something went wrong while responding to request: ' + error.error.message);
      }
    });
  }
}
