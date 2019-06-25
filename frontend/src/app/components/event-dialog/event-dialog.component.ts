import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Event} from '../../dtos/event';
import {Artist} from '../../dtos/artist';
import {ArtistResultsService} from '../../services/search-results/artists/artist-results.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {EventType} from '../../datatype/event_type';

@Component({
  selector: 'app-event-dialog',
  templateUrl: './event-dialog.component.html',
  styleUrls: ['./event-dialog.component.scss']
})
export class EventDialogComponent implements OnInit, OnChanges {

  @Input() title: string;
  @Input() event: Event;
  @Output() submitEvent = new EventEmitter<Event>();
  private error: boolean = false;
  private errorMessage: string = '';
  private eventForm: FormGroup;
  private eventType: string = null;
  private eventModel: Event = new Event(null, null, null, null, null, new Artist(null, null), null);
  private options: string[] = [];
  private submitted: boolean = false;
  private eventTypes = ['', 'Theatre', 'Opera', 'Festival', 'Concert', 'Movie', 'Musical', 'Sport', 'Other'];
  private config = {
    displayKey: 'Select an artist',
    search: true,
    height: '350px',
    placeholder: ' ',
    limitTo: this.options.length,
    moreText: 'more',
    noResultsFound: 'No results found!',
    searchPlaceholder: 'Search',
    searchOnKey: 'name'
  };

  constructor(private artistResultsService: ArtistResultsService, private formBuilder: FormBuilder) {
    this.eventForm = this.formBuilder.group({
      eventName: ['', [Validators.required]],
      type: ['', [Validators.required]],
      duration: ['', [Validators.required, Validators.min(1)]],
      artist: ['', [Validators.required]],
      content: ['', []],
      description: ['', []],
    });
  }

  private onSubmit() {
    this.submitted = true;
    if (this.eventForm.valid) {
      this.eventModel.eventType = EventType[this.eventType.toUpperCase()];
      this.submitEvent.emit(this.eventModel);
    } else {
      console.log('Invalid input');
    }
  }

  ngOnInit() {
    this.loadArtists();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['event'] && this.event !== undefined && this.title.includes('Edit')) {
      this.eventModel = Object.assign({}, this.event);
      const type = EventType[this.eventModel.eventType];
      this.eventType = type[0] + type.slice(1, type.length).toLocaleLowerCase();
    }
  }

  public loadArtists() {
    console.log('Load Artists');
    this.artistResultsService.findArtists('-1', 0).subscribe(
      (artists: Artist[]) => { this.options = this.artistArrayToStringArray(artists); },
      error => { this.defaultServiceErrorHandling(error); }
    );
  }

  private artistArrayToStringArray(artists: Artist[]): string[] {
    const toReturn: string[] = [];
    artists['content'].forEach(artist => { toReturn.push(artist.name); });
    return toReturn;
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
