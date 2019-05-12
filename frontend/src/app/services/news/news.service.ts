import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {News} from '../../dtos/news';
import {Observable} from 'rxjs';
import {Globals} from '../../global/globals';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  private newsBaseUri: string = this.globals.backendUri + '/news';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all news from the backend
   */
  getNews(): Observable<News[]> {
    return this.httpClient.get<News[]>(this.newsBaseUri);
  }

  /**
   * Loads all unread news from the backend
   */
  getUnreadNews(): Observable<News[]> {
    console.log('Get unread news');
    return this.httpClient.get<News[]>(this.newsBaseUri + '/unread');
  }


  /**
   * Loads specific news from the backend
   * @param id of news to load
   */
  getNewsById(id: number): Observable<News> {
    console.log('Load news details for ' + id);
    return this.httpClient.get<News>(this.newsBaseUri + '/' + id);
  }

  /**
   * Persists news to the backend
   * @param news to persist
   */
  createNews(news: News): Observable<News> {
    console.log('Create news with title ' + news.title);
                        console.log(news);
    return this.httpClient.post<News>(this.newsBaseUri, news);
  }

  /**
   * Confirms news fetch to the backend
   */
  updateNewsFetch(): void {
    console.log('Update news fetch and mark article as read');
    this.httpClient.put(this.newsBaseUri + '/news-fetch',  {});
  }
}
