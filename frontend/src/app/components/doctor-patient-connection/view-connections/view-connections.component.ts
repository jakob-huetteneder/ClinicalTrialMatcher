import {Component, OnInit} from '@angular/core';
import {TreatsService} from '../../../services/treats.service';
import {Treats, TreatsStatus} from '../../../dtos/patient';
import {Role} from '../../../dtos/role';
import {ActivatedRoute} from '@angular/router';
import {PatientService} from '../../../services/patient.service';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';

@Component({
  selector: 'app-view-connections',
  templateUrl: './view-connections.component.html',
  styleUrls: ['./view-connections.component.scss']
})
export class ViewConnectionsComponent implements OnInit {

  search = '';
  debouncer = new Subject<any>();

  userRole: Role;
  accepted: ConnectionEntry[] = [];

  constructor(
    private patientService: PatientService,
    private treatsService: TreatsService,
    private route: ActivatedRoute,
  ) {
  }

  public get role(): typeof Role {
    return Role;
  }


  get isEmpty(): boolean {
    return this.accepted.length === 0;
  }

  get emptyListText(): string {
    if (this.search === '') {
      return 'No connections found.';
    } else {
      return 'No connections found for <b>' + this.search + '</b>.';
    }
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.userRole = data.role;
      this.loadTreats();
    });

    this.debouncer.pipe(
      debounceTime(350),
      distinctUntilChanged()).subscribe(
      () => {
        console.log('now searching for: ' + this.search);
        this.loadTreats();
      });
  }

  delete(connection: ConnectionEntry) {
    this.treatsService.deleteTreats(connection.id).subscribe({
      next: () => {
        this.accepted = this.accepted.filter(entry => entry.id !== connection.id);
      },
      error: error => {
        console.log('Something went wrong while deleting connection: ' + error.error.message);
      }
    });
  }

  searchChanged() {
    console.log('search', this.search);
    this.debouncer.next(this.search);
  }

  private loadTreats() {
    this.treatsService.getAllRequests(this.search).subscribe({
      next: (requests: Treats[]) => {
        this.accepted = [];
        const acceptedTreats = requests.filter(request => request.status === TreatsStatus.accepted);
        console.log(acceptedTreats);
        if (this.userRole === Role.doctor) {
          acceptedTreats.forEach(treats => {
            const entry = new ConnectionEntry();
            entry.id = treats.patient.id;
            entry.firstName = treats.patient.firstName;
            entry.lastName = treats.patient.lastName;
            entry.email = treats.patient.email;
            entry.link = '/doctor/view-patient/' + treats.patient.id;
            this.accepted.push(entry);
          });
        } else {
          acceptedTreats.forEach(treats => {
            const entry = new ConnectionEntry();
            entry.id = treats.doctor.id;
            entry.firstName = treats.doctor.firstName;
            entry.lastName = treats.doctor.lastName;
            entry.email = treats.doctor.email;
            entry.link = undefined;
            this.accepted.push(entry);
          });
        }
        console.log(this.accepted);
      },
      error: error => {
        console.log('Something went wrong while loading requests: ' + error.error.message);
      }
    });
  }
}

class ConnectionEntry {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  link: string;
}
