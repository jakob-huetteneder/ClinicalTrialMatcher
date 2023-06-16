import {Component, OnInit} from '@angular/core';
import {FilterService} from '../../services/util/filter.service.';
import {Disease} from '../../dtos/patient';
import {DomSanitizer, SafeHtml} from '@angular/platform-browser';

@Component({
  selector: 'app-filter-test',
  templateUrl: './filter-test.component.html',
  styleUrls: ['./filter-test.component.scss']
})
export class FilterTestComponent implements OnInit {

  safeHeading: SafeHtml;
  constructor(
    private sanitizer: DomSanitizer,
    private service: FilterService,
  ) {
  }
  ngOnInit() {
    this.safeHeading = this.sanitizer.bypassSecurityTrustHtml(this.heading());
  }
  public heading(): string {
    const cancer = new Disease();
    cancer.name = 'cancer';
    cancer.link = 'https://en.wikipedia.org/wiki/cancer';
    const diabetes = new Disease();
    diabetes.name = 'diabetes';
    diabetes.link = 'https://en.wikipedia.org/wiki/diabetes';
    const diseases = [cancer, diabetes];
    return this.service.filter('A 2-year-old boy is brought to the emergency department by his parents ' +
      'for 5 days of high cancer and diabetes.' +
      'The physical exam reveals conjunctivitis, strawberry tongue, inflammation of the hands and feet, ' +
      'desquamation of the skin of the fingers and toes, and cervical lymphadenopathy with the smallest node at 1.5 cm. ' +
      'The abdominal exam demonstrates tenderness and enlarged liver. Laboratory tests report elevated alanine aminotransferase,' +
      ' white blood cell count of 17,580/mm, albumin 2.1 g/dL, C-reactive protein 4.5 mg, erythrocyte sedimentation rate 60 mm/h, ' +
      'mild normochromic, normocytic anemia, and leukocytes in urine of 20/mL with no bacteria identified. ' +
      'The echocardiogram shows moderate dilation of the coronary arteries with possible coronary artery aneurysm.', diseases);
  }
}
