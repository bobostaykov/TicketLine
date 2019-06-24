import {ErrorHandler} from '@angular/core';
import {GlobalErrorHandler} from './global-error-handler';

/**error handling provider*/
export const errorHandlingProvider = [
  {provide: ErrorHandler, useClass: GlobalErrorHandler}
];
