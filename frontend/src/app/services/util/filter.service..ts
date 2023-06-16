import {Injectable} from '@angular/core';
import {Disease} from '../../dtos/patient';

@Injectable({
  providedIn: 'root'
})
export class FilterService {
  constructor(
  ) {
  }

  public filter(text: string, diseases: Disease[]): string {
    diseases.forEach((d: Disease) => {
      console.log(d.name);
      console.log(d.link);
      text = text.replace(d.name, '<a href="' + d.link + '">' + d.name + '</a>');
      console.log(text);
    });
    return text;
  }
}
