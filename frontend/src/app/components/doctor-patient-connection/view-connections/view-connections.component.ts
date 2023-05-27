import {Component, OnInit} from '@angular/core';
import {TreatsService} from '../../../services/treats.service';
import {Treats, TreatsStatus} from '../../../dtos/patient';
import {Role} from '../../../dtos/role';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-view-connections',
  templateUrl: './view-connections.component.html',
  styleUrls: ['./view-connections.component.scss']
})
export class ViewConnectionsComponent implements OnInit {

  userRole: Role;
  persons: Person[] = [];
  constructor(
    private treatsService: TreatsService,
    private route: ActivatedRoute,
  ) { }

  public get role(): typeof Role {
    return Role;
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.userRole = data.role;
      this.loadPersons();
    });
  }

  delete(person: Person) {
    this.treatsService.deleteTreats(person.id).subscribe({
      next: () => {
        this.loadPersons();
      },
      error: error => {
        console.log('Something went wrong while deleting connection: ' + error.error.message);
      }
    });
  }

  private loadPersons() {
    this.treatsService.getAllRequests().subscribe({
      next: (treats: Treats[]) => {
        treats = treats.filter(treat => treat.status === TreatsStatus.accepted);
        if (this.userRole === Role.doctor) {
          this.persons = treats.map(treat => new Person(
              treat.patient.id,
              treat.patient.firstName,
              treat.patient.lastName,
              treat.patient.email
            ));
        } else if (this.userRole === Role.patient) {
          this.persons = treats.map(treat => new Person(
              treat.doctor.id,
              treat.doctor.firstName,
              treat.doctor.lastName,
              treat.doctor.email
            ));
        }
        console.log(treats);
      },
      error: error => {
        console.log('Something went wrong while loading requests: ' + error.error.message);
      }
    });

  }
}


class Person {
  id: number;
  firstName: string;
  lastName: string;
  email: string;

  constructor(id: number, firstName: string, lastName: string, email: string) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
}
