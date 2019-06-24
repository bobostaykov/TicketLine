import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBarRef} from '@angular/material';

@Component({
  selector: 'app-error-snack-bar',
  templateUrl: './error-snack-bar.component.html',
  styleUrls: ['./error-snack-bar.component.scss']
})
export class ErrorSnackBarComponent implements OnInit {

  constructor(@Inject(MAT_SNACK_BAR_DATA) private messages: string[], private snackBarRef: MatSnackBarRef<ErrorSnackBarComponent>) { }

  ngOnInit() {
  }

  private closeSnackBar() {
    this.snackBarRef.dismissWithAction();
  }
}
