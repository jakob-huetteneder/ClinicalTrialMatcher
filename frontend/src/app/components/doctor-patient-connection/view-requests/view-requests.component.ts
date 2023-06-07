import {Component, OnInit} from '@angular/core';
import {Treats, TreatsStatus} from '../../../dtos/patient';
import {TreatsService} from '../../../services/treats.service';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';

@Component({
  selector: 'app-doctor-requests',
  templateUrl: './view-requests.component.html',
  styleUrls: ['./view-requests.component.scss']
})
export class ViewRequestsComponent implements OnInit {

  search = '';
  debouncer = new Subject<any>();

  requested: Treats[] = [];
  accepted: Treats[] = [];
  declined: Treats[] = [];

  constructor(
    private treatsService: TreatsService,
  ) {
  }

  get isEmpty(): boolean {
    return this.requested.length === 0 && this.accepted.length === 0 && this.declined.length === 0;
  }

  get emptyListText(): string {
    if (this.search === '') {
      return 'No requests found.';
    } else {
      return 'No requests found for <b>' + this.search + '</b>.';
    }
  }

  ngOnInit(): void {
    this.loadTreats();

    this.debouncer.pipe(
      debounceTime(350),
      distinctUntilChanged()).subscribe(
      () => {
        console.log('now searching for: ' + this.search);
        this.loadTreats();
      });
  }

  accept(request: Treats) {
    this.respondToRequest(request, true);
  }

  decline(request: Treats) {
    this.respondToRequest(request, false);
  }

  delete(treats: Treats) {
    this.treatsService.deleteTreats(treats.doctor.id).subscribe({
      next: () => {
        this.loadTreats();
      },
      error: error => {
        console.log('Something went wrong while deleting connection: ' + error.error.message);
      }
    });
  }

  searchChanged(event: any) {
    this.search = event.target.value;
    console.log('search', this.search);
    this.debouncer.next(event);
  }

  private loadTreats() {
    this.treatsService.getAllRequests(this.search).subscribe({
      next: (requests: Treats[]) => {
        this.requested = requests.filter(request => request.status === TreatsStatus.requested);
        this.accepted = requests.filter(request => request.status === TreatsStatus.accepted);
        this.declined = requests.filter(request => request.status === TreatsStatus.declined);
        console.log(requests);
      },
      error: error => {
        console.log('Something went wrong while loading requests: ' + error.error.message);
      }
    });
  }

  private respondToRequest(request: Treats, accept: boolean) {
    console.log(request);
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
