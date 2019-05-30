export class News {
  constructor(
    public id: number,
    public title: string,
    public summary: string,
    public text: string,
    public image: string,
    public imageURL: string,
    public publishedAt: string) {
  }
}
