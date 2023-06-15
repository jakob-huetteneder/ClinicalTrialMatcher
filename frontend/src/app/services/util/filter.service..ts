import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DiseaseService} from 'src/app/services/disease.service';
import {AnalyzerService} from '../analyzer.service';

@Injectable({
  providedIn: 'root'
})
export class FilterService {
  constructor(
    private http: HttpClient,
    private diseaseService: DiseaseService,
    private analyzerService: AnalyzerService
  ) {
  }

  // temporary method, necessary for second workflow
  public filter(text: string): string {
    this.analyzerService.analyzeNote(text).subscribe({
      next: data => {
        console.log(data);
        data.forEach((d: string) => {
          let link: string;
          this.diseaseService.searchByName(d, 1).subscribe(tmpdiseases => link = tmpdiseases[0].link);
          text.replace(d, '<a href="' + link + '">' + d + '</a>');
        });
      },
      error: error => {
        console.log('Error filtering text: ' + error);
      }
    });
    return text;
  }
}
