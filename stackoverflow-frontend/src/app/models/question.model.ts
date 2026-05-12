export interface Question {
  id: number;
  title: string;
  text: string,
  author: string,
  votes: number,
  tags: string[],
  createdAt: string;
}
