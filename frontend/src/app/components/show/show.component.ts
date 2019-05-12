import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Show} from '../../dtos/show';
import {ResultsFor} from '../../datatype/results_for';

@Component({
  selector: 'app-shows',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.scss']
})
export class ShowComponent implements OnInit {

  private error: boolean = false;
  private errorMessage: string = '';
  private resultsFor: ResultsFor;
  private name: string;
  private shows: Show[];
  private headElements: string[] = [
    'Event',
    'Artist',
    'Location',
    'Hall',
    'City - Country',
    'Date',
    'Category',
    'Sold Tickets',
    'Buy'
  ];

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    const param = this.route.snapshot.queryParamMap.get('results_for');
    this.resultsFor = param !== null ? ResultsFor[param] : null;
    this.name = this.route.snapshot.queryParamMap.get('name');
  }


  /**
   * Convert the resultsFor variable to a string with only first letter capital
   */
  private toWord(resultsFor: ResultsFor): string {
    let asString = ResultsFor[resultsFor];
    asString = asString[0] + asString.slice(1, asString.length).toLocaleLowerCase();
    return asString;
  }


  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.message !== 'No message available') {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.error;
    }
  }

}
