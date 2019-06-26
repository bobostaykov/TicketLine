import {Injectable, NgZone} from '@angular/core';
import {MatSnackBar} from '@angular/material';
import {ErrorSnackBarComponent} from '../../components/error-snack-bar/error-snack-bar.component';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(public snackBar: MatSnackBar, public ngZone: NgZone) { }

  /**
   * displays message as error in the Angular material snackbar
   * @param message to be displayed
   */
  displayError(message: string[]): void {
    // without this workaround (found on github, by user Charius) the snack bar initially appears in the wrong position for some reason
    this.ngZone.run(() => {
      const snackbar = this.snackBar.openFromComponent(ErrorSnackBarComponent, {
        verticalPosition: 'bottom',
        horizontalPosition: 'center',
        data: message,
        duration: 5000
      });
      snackbar.onAction().subscribe(() => {
        snackbar.dismiss();
      });
    });
  }
}
