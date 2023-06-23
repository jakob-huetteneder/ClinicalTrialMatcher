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

    // Create a pattern of all disease names joined with '|'
    const diseaseNamesPattern = diseases.map(d => d.name.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&')).join('|');

    const regex = new RegExp(`(?<!\\w)(${diseaseNamesPattern})(?!\\w)`, 'gi');

    text = text.replace(regex, (match: string) => {
      const matchedDisease = diseases.find(d => d.name.toLowerCase() === match.toLowerCase());
      if (matchedDisease && matchedDisease.link !== null) {
        return `<a class="text-blue-500 font-semibold after:content-[\'_↗\'] after:text-sm after:font-bold no-underline"
              style="white-space: nowrap;" href="${matchedDisease.link}">${match}</a>`;
      }
      return match;
    });

    return text;
  }

}
