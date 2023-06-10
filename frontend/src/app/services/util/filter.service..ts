import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DiseaseService} from 'src/app/services/disease.service';
import {Disease} from '../../dtos/patient';

@Injectable({
  providedIn: 'root'
})
export class FilterService {
  constructor(
    private http: HttpClient,
    private diseaseService: DiseaseService,
    private diseases: Disease[] = []
  ) {
  }

  public filter(text: string): string {
    const words = text.split(' ');
    for (let i = 0; i < words.length; i++) {
      this.diseaseService.searchByName(words[i], 1).subscribe(diseases => this.diseases = diseases);
      if (this.diseases.length === 1) {
        const link = this.getLink(this.diseases[0].name);
        if (link !== '') {
          words[i] = '<a href="' + link + '">' + words[i] + '</a>';
        }
      }
    } return words.join(' ');
  }

  public getLink(query: string): string {
    fetch(`https://api.wikimedia.org/core/v1/wikipedia/en/page/${query}/bare`, {
      mode: 'cors',
      method: 'GET'
    }).then(response => {
      if (response.ok) {
        response.json().then(json => {
          //result = json;
          const {html_url: myUrl} = json;
          return myUrl;
        });
      } else {
        return '';
      }
    })
      .catch(err => {
        console.log('Fetch Error: ', err);
        return '';
      });
    return '';
  }
}
