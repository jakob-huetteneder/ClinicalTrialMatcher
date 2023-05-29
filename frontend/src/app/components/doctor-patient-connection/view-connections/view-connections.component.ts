import {Component, OnInit} from '@angular/core';
import {TreatsService} from '../../../services/treats.service';
import {Treats, TreatsStatus} from '../../../dtos/patient';
import {Role} from '../../../dtos/role';
import {ActivatedRoute} from '@angular/router';
import {PatientService} from '../../../services/patient.service';

@Component({
  selector: 'app-view-connections',
  templateUrl: './view-connections.component.html',
  styleUrls: ['./view-connections.component.scss']
})
export class ViewConnectionsComponent implements OnInit {

  userRole: Role;
  accepted: Treats[] = [];
  constructor(
    private patientService: PatientService,
    private treatsService: TreatsService,
    private route: ActivatedRoute,
  ) { }

  public get role(): typeof Role {
    return Role;
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.userRole = data.role;
      this.loadTreats();
    });
  }
  delete(treats: Treats) {
    this.treatsService.deleteTreats(treats.patient.id).subscribe({
      next: () => {
        this.loadTreats();
      },
      error: error => {
        console.log('Something went wrong while deleting connection: ' + error.error.message);
      }
    });
  }

  private loadTreats() {
    this.treatsService.getAllRequests().subscribe({
      next: (requests: Treats[]) => {
        this.accepted = requests.filter(request => request.status === TreatsStatus.accepted);
        console.log(requests);
      },
      error: error => {
        console.log('Something went wrong while loading requests: ' + error.error.message);
      }
    });
  }
}
