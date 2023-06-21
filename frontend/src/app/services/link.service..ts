import {Injectable} from '@angular/core';
import {Disease} from '../dtos/patient';

@Injectable({
  providedIn: 'root'
})
export class LinkService {
  constructor(
  ) {
  }

  public filter(text: string, diseases: Disease[]): string {
    if (!text || !diseases) {
      return text;
    }
    diseases.forEach((d: Disease) => {
      console.log(d.name);
      console.log(d.link);
      text = text.replace(d.name, '<a href="' + d.link + '">' + d.name + '</a>');
      console.log(text);
    });
    return text;
  }
}
