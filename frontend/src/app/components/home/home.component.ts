import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NgModel} from '@angular/forms';
import {Filter} from 'src/app/dtos/filter';
import {TrialService} from '../../services/trial.service';
import {Trial} from '../../dtos/trial';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

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
    startDate: null
  };

  constructor(public authService: AuthService, private trialService: TrialService, private notification: ToastrService) {
  }

  ngOnInit() {
    this.searchWithFilter();
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      // This names in this object are determined by the style library,
      // requiring it to follow TypeScript naming conventions does not make sense.
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'is-invalid': !input.valid && !input.pristine,
    };
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

  public searchWithFilter() {
    this.searching = true;
    this.trialService.searchForTrialWithFilter(this.keyword, this.filter).subscribe({
      next: (trials: Trial[]) => {
        this.searching = false;
        this.trials = trials;
      },
      error: error => {
        console.log('Something went wrong while loading users: ' + error.error.message);
        this.notification.error(error.error.message, 'Something went wrong while loading users');
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
    this.searchWithFilter();
  }

}
