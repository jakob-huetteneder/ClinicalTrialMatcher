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

  lipsum: string = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit,' +
    '      sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.' +
    '      Amet facilisis magna etiam tempor orci eu.' +
    '      Cursus vitae congue mauris rhoncus aenean vel elit scelerisque.' +
    '      At consectetur lorem donec massa sapien faucibus et. Mi ipsum faucibus vitae aliquet.' +
    '      In hendrerit gravida rutrum quisque non tellus orci ac auctor.' +
    '      Ut diam quam nulla porttitor massa id neque aliquam.' +
    '      Dui accumsan sit amet nulla facilisi morbi tempus iaculis.' +
    '      Sollicitudin aliquam ultrices sagittis orci a scelerisque...';

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
      this.lipsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit,' +
        '      sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.' +
        '      Amet facilisis magna etiam tempor orci eu.' +
        '      Cursus vitae congue mauris rhoncus aenean vel elit scelerisque.' +
        '      At consectetur lorem donec massa sapien faucibus et. Mi ipsum faucibus vitae aliquet.' +
        '      In hendrerit gravida rutrum quisque non tellus orci ac auctor.' +
        '      Ut diam quam nulla porttitor massa id neque aliquam.' +
        '      Dui accumsan sit amet nulla facilisi morbi tempus iaculis.' +
        '      Sollicitudin aliquam ultrices sagittis orci a scelerisque...';
      document.getElementById('moreInfo').innerHTML = 'More Info';
    } else {
      this.lipsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit,' +
        '      sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.' +
        '      Amet facilisis magna etiam tempor orci eu.' +
        '      Cursus vitae congue mauris rhoncus aenean vel elit scelerisque.' +
        '      At consectetur lorem donec massa sapien faucibus et. Mi ipsum faucibus vitae aliquet.' +
        '      In hendrerit gravida rutrum quisque non tellus orci ac auctor.' +
        '      Ut diam quam nulla porttitor massa id neque aliquam.' +
        '      Dui accumsan sit amet nulla facilisi morbi tempus iaculis.' +
        '      Sollicitudin aliquam ultrices sagittis orci a scelerisque purus semper eget.' +
        '      Purus sit amet luctus venenatis lectus magna.' +
        '      Condimentum vitae sapien pellentesque habitant morbi tristique.' +
        '      Arcu felis bibendum ut tristique et egestas quis.' +
        '      At ultrices mi tempus imperdiet nulla malesuada pellentesque.' +
        '      Condimentum vitae sapien pellentesque habitant.' +
        '      Pulvinar neque laoreet suspendisse interdum consectetur libero.' +
        '      Nisl tincidunt eget nullam non nisi est.';
      document.getElementById('moreInfo').innerHTML = 'Less Info';
    }
    document.getElementById('li').innerHTML = this.lipsum;
  }

  public isSearching() {
    return this.searching;
  }

  public isAdvancedSearch() {
    return this.advanced;
  }

  public search() {
    this.searching = true;
    this.trialService.searchForTrial(this.keyword).subscribe({
      next: (trials: Trial[]) => {
        this.trials = trials;
      },
      error: error => {
        console.log('Something went wrong while loading users: ' + error.error.message);
        this.notification.error(error.error.message, 'Something went wrong while loading users');
        console.log(error);
      }
    });
  }

  public searchWithFilter() {
    this.searching = true;
    this.trialService.searchForTrialWithFilter(this.keyword, this.filter).subscribe({
      next: (trials: Trial[]) => {
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

  private loadTrials() {
    this.trialService.getAll().subscribe({
      next: (trials: Trial[]) => {
        this.trials = trials;
      },
      error: error => {
        console.log('Something went wrong while loading trials: ' + error.error.message);
        this.notification.error(error.error.message, 'Something went wrong while loading trials');
        console.log(error);
      }
    });
  }

}
