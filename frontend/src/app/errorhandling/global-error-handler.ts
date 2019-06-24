import {ErrorHandler, Injectable, Injector} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {ErrorService} from '../services/error/error.service';
import {NotificationService} from '../services/notification/notification.service';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

  constructor(private injector: Injector) {
  }

  handleError(error: Error | HttpErrorResponse): void {
    const errorService = this.injector.get(ErrorService);
    const notificationService = this.injector.get(NotificationService);
    if (error instanceof HttpErrorResponse) {
      notificationService.displayError(errorService.getServerErrorAsArray(error));
    }
    console.log(error);
  }
}
