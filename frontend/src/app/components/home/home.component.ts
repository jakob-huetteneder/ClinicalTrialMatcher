import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Filter} from 'src/app/dtos/filter';
import {TrialService} from '../../services/trial.service';
import {Trial} from '../../dtos/trial';
import {ToastrService} from 'ngx-toastr';
import {debounceTime, Subject} from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  searchSubject = new Subject<string>();
  filterSubject = new Subject<Filter>();

  trials: Trial[] = [];

  keyword = '';

  showMoreInfo = false;

  searching = false;

  advanced = false;

  filter: Filter = {
    gender: null,
    recruiting: null,
    age: null,
    endDate: null,
    startDate: null,
    page: 1,
    size: 10,
  };
  totalElements = -1;

  constructor(public authService: AuthService, private trialService: TrialService,
              private notification: ToastrService, private elementRef: ElementRef, private renderer: Renderer2) {
    this.searchSubject
      .pipe(debounceTime(500)) // Adjust the debounce time as desired (in milliseconds)
      .subscribe(() => {
        this.searchWithFilter(1);
      });
    this.filterSubject
      .pipe(debounceTime(500))
      .subscribe(() => {
        this.searchWithFilter(1);
      });
  }

  ngOnInit() {
    this.searchWithFilter(1);
  }

  onKeywordChange(): void {
    this.searchSubject.next(this.keyword);
  }

  onFilterChange(): void {
    this.filterSubject.next(this.filter);
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

  public nextPage(page: number) {
    if (page !== undefined) {
      this.scrollToFirstItem();
    }
    this.searchWithFilter(page);
  }

  public searchWithFilter(page: number) {
    if (page !== undefined) {
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

  scrollToFirstItem() {
    const firstItem = document.querySelector('app-trial-list-item');
    if (firstItem) {
      this.renderer.setStyle(document.documentElement, 'scrollBehavior', 'smooth');
      const rect = firstItem.getBoundingClientRect();
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
      const elementTop = rect.top + scrollTop;
      this.renderer.setProperty(document.documentElement, 'scrollTop', elementTop);
    }
  }


  public resetFilter() {
    this.filter = {
      endDate: null,
      gender: null,
      age: null,
      recruiting: null,
      startDate: null,
      page: this.filter.page,
      size: this.filter.size
    };
    this.searchWithFilter(1);
  }

}
