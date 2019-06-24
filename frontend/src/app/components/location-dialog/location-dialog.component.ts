import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Location} from '../../dtos/location';

@Component({
  selector: 'app-location-dialog',
  templateUrl: './location-dialog.component.html',
  styleUrls: ['./location-dialog.component.scss']
})
export class LocationDialogComponent implements OnInit, OnChanges {

  @Input() title: string;
  @Input() location: Location;
  @Output() submitLocation = new EventEmitter<Location>();
  private locationModel: Location = new Location(null, null, null, null, null, null, null);

  constructor() {
  }

  /**
   * on submission of form emit event with location to listening parent
   */
  private onSubmit() {
    this.submitLocation.emit(this.locationModel);
  }

  ngOnInit() {}

  /**
   * runs on change of input variable location
   * updates locationModel linked to form with location input
   * @param changes list of changes to component
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['location']) {
      if (this.location !== undefined) {
        this.locationModel = Object.assign({}, this.location);
      }
    }
  }
}
