import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Chart, registerables} from 'chart.js';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';
import {TrialService} from '../../../services/trial.service';
import {TrialRegistration, TrialRegistrationStatus} from '../../../dtos/trial';
import {Gender} from '../../../dtos/gender';
import {differenceInYears, format, parseISO} from 'date-fns';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {
  @ViewChild('gender') genderChart!: ElementRef<HTMLCanvasElement>;
  chart1!: Chart<'pie', number[], unknown>;
  // eslint-disable-next-line @typescript-eslint/member-ordering
  @ViewChild('date1') date1Chart!: ElementRef<HTMLCanvasElement>;
  chart2!: Chart<'line', number[], unknown>;
  // eslint-disable-next-line @typescript-eslint/member-ordering
  @ViewChild('status') statusChart!: ElementRef<HTMLCanvasElement>;
  chart3!: Chart<'pie', number[], unknown>;
  // eslint-disable-next-line @typescript-eslint/member-ordering
  @ViewChild('age') ageChart!: ElementRef<HTMLCanvasElement>;
  chart4!: Chart<'bar', number[], unknown>;
  id = -1;
  registrations: TrialRegistration[] = [];
  title = '';
  minAge: number;
  maxAge: number;
  acceptedVisible = false;

  constructor(
    private service: TrialService,
    private notification: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    Chart.register(...registerables);

    this.route.params.subscribe(params => {
      this.id = params.id;
      this.service.getAllRegistrationsForTrial(this.id).subscribe({
        next: data => {
          this.registrations = data.filter(i => i.status !== TrialRegistrationStatus.proposed);
          this.service.getById(this.id).subscribe({
            next: value => {
              this.title = value.title;
              this.maxAge = value.crMaxAge;
              this.minAge = value.crMinAge;
              if (this.registrations.length === 0) {
                this.notification.info('No registrations yet for this trial!', '');
                this.router.navigate(['./researcher/trials']).then();
              } else {
                this.initCharts();
              }
            },
            error: err => {
              console.error('Error, trial does not exist', err);
              this.notification.error(err.error.message, 'Error fetching clinical trial');
              return this.router.navigate(['']);
            }
          });
        },
        error: error => {
          console.error('Error, trial does not exist', error);
          this.notification.error(error.error.message, 'Error fetching clinical trial');
          return this.router.navigate(['']);
        }
      });
    });
  }

  initCharts() {
    let ctx = this.genderChart.nativeElement.getContext('2d');

    const accepted = this.registrations.filter(i => i.status === TrialRegistrationStatus.accepted);

    if (accepted.length !== 0) {
      this.acceptedVisible = true;
    }

    this.chart1 = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: ['Female', 'Male'],
        datasets: [
          {
            label: '# of accepted registrations',
            data: [this.registrations.filter((item) => item.patient.gender === Gender.female &&
              item.status === TrialRegistrationStatus.accepted).length,
              this.registrations.filter((item) => item.patient.gender === Gender.male &&
                item.status === TrialRegistrationStatus.accepted).length],
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(54, 162, 235, 0.2)'
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)',
              'rgba(54, 162, 235, 1)'
            ],
            borderWidth: 1,
          },
        ],
      },
    });

    ctx = this.date1Chart.nativeElement.getContext('2d');

    const dateList: Date[] = this.registrations.map((obj) => obj.date);
    dateList.sort((a: Date, b: Date) => {
      if (a < b) {
        return -1;
      }
      if (a > b) {
        return 1;
      }
      return 0;
    });

    const dateCounts: number[] = [];
    let prev;

    // eslint-disable-next-line @typescript-eslint/prefer-for-of
    for (let i = 0; i < dateList.length; i++) {
      if (prev !== undefined && prev === dateList[i]) {
        dateCounts[dateCounts.length - 1]++;
      } else {
        dateCounts.push(1);
        prev = dateList[i];
      }
    }

    // Create a Set from the dateList to automatically remove duplicates
    const uniqueDatesSet: Set<Date> = new Set(dateList);

    // Convert the Set back to an array
    const uniqueDates: Date[] = Array.from(uniqueDatesSet);

    uniqueDates.sort((a: Date, b: Date) => {
      if (a < b) {
        return -1;
      }
      if (a > b) {
        return 1;
      }
      return 0;
    });

    const datePipe = new DatePipe('en-US');

    const formattedDates: string[] = uniqueDates.map((date) => datePipe.transform(date, 'dd.MM.yyyy') || '');


    this.chart2 = new Chart(ctx, {
      type: 'line',
      data: {
        labels: formattedDates,
        datasets: [
          {
            label: '# of registrations',
            data: dateCounts,
            backgroundColor: [
              'rgba(140, 98, 255, 0.2)'
            ],
            borderColor: [
              'rgba(140, 98, 255, 1)'
            ],
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1, // Set the step size to 1 to display only whole numbers
            }
          },
        },
      },
    });

    ctx = this.statusChart.nativeElement.getContext('2d');

    this.chart3 = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: ['Pending', 'Rejected', 'Accepted'],
        datasets: [
          {
            data: [this.registrations.filter((item) => item.status === TrialRegistrationStatus.patientAccepted).length,
              this.registrations.filter((item) => item.status === TrialRegistrationStatus.declined).length,
              this.registrations.filter((item) => item.status === TrialRegistrationStatus.accepted).length],
            backgroundColor: [
              'rgba(150, 150, 150, 0.2)',
              'rgba(255, 99, 132, 0.2)',
              'rgba(72, 187, 120, 0.2)'
            ],
            borderColor: [
              'rgba(150, 150, 150, 1)',
              'rgba(255, 99, 132, 1)',
              'rgba(72, 187, 120, 1)'
            ],
            borderWidth: 1,
          },
        ],
      }
    });

    ctx = this.ageChart.nativeElement.getContext('2d');

    const ageList = accepted.map(i => differenceInYears(new Date(Date.now()), parseISO('' + i.patient.birthdate)));
    const step = parseFloat(Math.abs((this.maxAge - this.minAge) / 10).toFixed(0));
    const labels: string[] = [];
    const values: number[] = [];
    for (let i = this.minAge; i <= this.maxAge; i += step) {
      labels.push(i.toFixed(0) + '-' + (i + (step)).toFixed(0));
      values.push(ageList.filter(item => item >= i && item < (i + step)).length);
    }

    this.chart4 = new Chart(ctx, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          {
            label: '# in range',
            data: values,
            backgroundColor: [
              'rgba(255, 165, 0, 0.2)'
            ],
            borderColor: [
              'rgba(255, 165, 0, 1)'
            ],
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1, // Set the step size to 1 to display only whole numbers
            }
          },
        },
      },
    });


  }
}
