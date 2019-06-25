import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Injectable, Injector} from '@angular/core';
import {ErrorService} from '../services/error/error.service';
import {NotificationService} from '../services/notification/notification.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private injector: Injector) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req)
      .pipe(
        catchError((error: HttpErrorResponse) => {
            const errorService = this.injector.get(ErrorService);
            const notificationService = this.injector.get(NotificationService);
            notificationService.displayError(errorService.getServerErrorAsArray(error));
            return throwError(error as HttpErrorResponse);
          }
        )
      );
  }
}

