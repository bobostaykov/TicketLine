import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-top-ten-events',
  templateUrl: './top-ten-events.component.html',
  styleUrls: ['./top-ten-events.component.scss']
})
export class TopTenEventsComponent implements OnInit {

  categories: String[] = ['all', 'concert', 'cabaret/comedy', 'sport', 'musical', 'theatre'];

  constructor() { }

  ngOnInit() {
  }

  getCategories() {
    return this.categories;
  }

}
