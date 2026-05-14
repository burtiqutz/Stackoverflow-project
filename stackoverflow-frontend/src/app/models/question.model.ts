export interface QuestionAuthor {
  id: number;
  username?: string;
  name?: string;
  email?: string;
}

export interface Question {
  id: number;
  author?: QuestionAuthor;
  title: string;
  text: string;
  image?: string;
  createdAt: string;
  status?: string;
  tags: string[];
}
