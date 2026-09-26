export interface ApiError {
  timestamp: string;
  status:    number;
  error:     string;
  errorCode: string;
  message:   string;
  path:      string;
  errors?:   { [key: string]: string };
}
