import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NgModel} from '@angular/forms';
import {Filter} from 'src/app/dtos/filter';
import {TrialService} from '../../services/trial.service';
import {Trial} from '../../dtos/trial';
import {ToastrService} from 'ngx-toastr';
import {debounceTime, Subject} from 'rxjs';
import {Collection} from 'ngx-pagination';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  searchSubject = new Subject<string>();

  trials: Trial[] = [];

  keyword = '';

  showMoreInfo = false;

  searching = false;

  advanced = false;

  filter: Filter = {
    gender: null,
    recruiting: null,
    minAge: null,
    maxAge: null,
    endDate: null,
    startDate: null,
    page: 1,
    size: 10,
  };
  totalElements = -1;

  constructor(public authService: AuthService, private trialService: TrialService, private notification: ToastrService) {
    this.searchSubject
      .pipe(debounceTime(500)) // Adjust the debounce time as desired (in milliseconds)
      .subscribe(() => {
        this.searchWithFilter(this.filter.page);
      });
  }

  ngOnInit() {
    this.searchWithFilter(1);
  }

  onKeywordChange(): void {
    this.searchSubject.next(this.keyword);
  }

  public moreInfo(): void {
    if (document.getElementById('moreInfo').innerHTML === 'Less Info') {
      document.getElementById('moreInfo').innerHTML = 'More Info';
      this.showMoreInfo = false;
    } else {
      document.getElementById('moreInfo').innerHTML = 'Less Info';
      this.showMoreInfo = true;
    }
  }

  public isSearching() {
    return this.searching;
  }

  public isAdvancedSearch() {
    return this.advanced;
  }

  public searchWithFilter(page: number) {
    if(page !== undefined) {
      this.filter.page = page;
    }
    this.searching = true;
    this.trialService.searchForTrialWithFilter(this.keyword, this.filter).subscribe({
      next: (trials) => {
        this.searching = false;
        this.trials = trials.content;
        this.totalElements = trials.totalElements;
        console.log(trials);
      },
      error: error => {
        console.log('Something went wrong while loading trials: ' + error.error.message);
        this.notification.error(error.error.message, 'Something went wrong while loading trials');
        console.log(error);
      }
    });
  }

  public resetFilter() {
    this.filter = {
      endDate: null,
      gender: null,
      maxAge: null,
      minAge: null,
      recruiting: null,
      startDate: null
    };
    this.searchWithFilter(1);
  }

}
