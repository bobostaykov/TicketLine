import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {NewsService} from '../../services/news/news.service';
import {News} from '../../dtos/news';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import * as _ from 'lodash';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth/auth.service';
import {FileService} from '../../services/file/file.service';
import {UserService} from '../../services/user/user.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit {


  private page: number = 0;
  private totalPages: number;
  private pageRange: Array<number> = [];
  private dataReady: boolean = false;

  error: boolean = false;
  errorMessage: string = '';
  image: File;
  newsForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted: boolean = false;
  private news: News[];
  showReadNews = false;
  NOT_FOUND_IMAGE_URL: string = '../../../assets/image_not_found.png';

  constructor(private newsService: NewsService, private fileService: FileService, private userService: UserService,
              private ngbPaginationConfig: NgbPaginationConfig, private formBuilder: FormBuilder,
              private cd: ChangeDetectorRef, private authService: AuthService) {
    this.newsForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      text: ['', [Validators.required]],
      image: ['']
    });
  }

  ngOnInit() {
    this.loadNews();
  }


  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.loadNews();
  }

  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      this.loadNews();
    }
  }

  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadNews();
    }
  }


  /**
   * Determines the page numbers which will be shown in the clickable menu
   */
  private setPagesRange() {
    this.pageRange = []; // nullifies the array
    if (this.totalPages <= 11) {
      for (let i = 0; i < this.totalPages; i++) {
        this.pageRange.push(i);
      }
    } else {
      if (this.page <= 5) {
        for (let i = 0; i <= 10; i++) {
          this.pageRange.push(i);
        }
      }
      if (this.page > 5 && this.page < this.totalPages - 5) {
        for (let i = this.page - 5; i <= this.page + 5; i++) {
          this.pageRange.push(i);
        }
      }
      if (this.page >= this.totalPages - 5) {
        for (let i = this.totalPages - 10; i < this.totalPages; i++) {
          this.pageRange.push(i);
        }
      }
    }
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  /**
   * Rads the input file into
   */
  processFile(imageInput: any) {
    this.image = imageInput.files[0];
  }

  /**
   * Starts form validation and builds a news dto for sending a creation request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  addNews() {
    this.submitted = true;
    if (this.newsForm.valid) {
      const news: News = new News(null,
        this.newsForm.controls.title.value,
        // this.newsForm.controls.summary.value,
        null,
        this.newsForm.controls.text.value,
        null,
        null,
        new Date().toISOString()
      );
      if (this.image != null) {
        this.fileService.uploadFile(this.image).subscribe(id => {
          news.image = id;
          this.createNews(news);
          },
          error => {
          }
          );
      } else {
        console.log('No image present');
        this.createNews(news);
      }
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Sends news creation request
   * @param news the news which should be created
   */
  createNews(news: News) {
    this.newsService.createNews(news).subscribe(
      () => {
        this.loadNews();
      },
      error => {
      }
    );
  }

  getNews(): News[] {
    return this.news;
  }

  /**
   * Shows the specified news details. If it is necessary, the details text will be loaded
   * @param id the id of the news which details should be shown
   */
  getNewsDetails(id: number) {
    if (_.isEmpty(this.news.find(x => x.id === id).text)) {
      this.loadNewsDetails(id);
    }
  }

  /**
   * Loads the text of news and update the existing array of news
   * @param id the id of the news which details should be loaded
   */
  loadNewsDetails(id: number) {
    this.newsService.getNewsById(id).subscribe(
      (news: News) => {
        const result = this.news.find(x => x.id === id);
        result.text = news.text;
        if (news.image != null) {
          this.fileService.getFile(news.image).subscribe(
            val => {
              console.log('Loaded file with id ' + news.image);
              const reader = new FileReader();
              reader.addEventListener('load', () => {
                result.imageURL = <string>reader.result; }, false);
              if (val) {
                reader.readAsDataURL(val);
              }
            },
            error => {
              result.imageURL = this.NOT_FOUND_IMAGE_URL;
            }
          );
        }
      },
      error => {
      }
    );
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  changeChecked() {
    this.showReadNews = !this.showReadNews;
    this.loadNews();
  }

  private loadNews() {
    if (this.showReadNews === true) {
      console.log('showReadNews: True');
    } else {
      console.log('showReadNews: False');
    }

    if (this.showReadNews === true) {
      this.newsService.getAllNews(this.page).subscribe(
        result => {
          this.news = result['content'];
          this.totalPages = result['totalPages'];
          this.setPagesRange();
        },
        error => {
        },
      () => { this.dataReady = true; }
      );
    } else {
      this.newsService.getUnreadNews(this.page).subscribe(
        result => {
          this.news = result['content'];
          this.totalPages = result['totalPages'];
          this.setPagesRange();
        },
        error => {
        },
        () => { this.dataReady = true; }
      );
    }
  }

  private clearForm() {
    this.newsForm.reset();
    this.image = null;
    this.submitted = false;
  }

}
