import {Injectable} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor() {
  }

  /**
   * retrieves error message sent from the backend and returns it as string array
   * to better display it later
   * @param error for which message should be returned
   */
  getServerErrorAsArray(error: HttpErrorResponse): string[] {
    const message: string[] = [];
    if (error.error) {
      if (error.error.errors) {
        error.error.errors.forEach(err => message.push(this.sanitizeMessage(err)));
        return message;
      }
      return [this.sanitizeMessage(error.error.message)];
    }
    return [this.sanitizeMessage(error.message)];
  }

  /**
   * used to replace everything until colon : to create better looking error messages
   * @param message to replace text in
   */
  private sanitizeMessage(message: string): string {
    return message.replace(new RegExp('^.*\:\\s*'), '');
  }
}

