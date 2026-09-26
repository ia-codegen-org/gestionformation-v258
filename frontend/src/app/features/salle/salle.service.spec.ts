import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { SalleService } from './salle.service';
import {
  SalleCreateDTO,
  SalleUpdateDTO,
  SalleResponseDTO
} from './models/salle.model';

describe('SalleService', () => {
  let service: SalleService;
  let httpMock: HttpTestingController;
  const apiUrl = '/api/salle';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SalleService]
    });
    service = TestBed.inject(SalleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('devrait être créé', () => {
    expect(service).toBeTruthy();
  });

  it('findAll() devrait récupérer la liste', () => {
    const mockData: SalleResponseDTO[] = [
      { id: 1 } as SalleResponseDTO
    ];

    service.findAll().subscribe(data => {
      expect(data).toEqual(mockData);
    });

    const req = httpMock.expectOne(r => r.url.includes(apiUrl) && r.method === 'GET' && !r.url.includes('/1') && !r.url.includes('/count'));
    req.flush(mockData);
  });

  it('count() devrait retourner le nombre total', () => {
    service.count().subscribe(data => {
      expect(data).toBe(5);
    });

    const req = httpMock.expectOne(r => r.url.includes('/count'));
    expect(req.request.method).toBe('GET');
    req.flush(5);
  });

  it('findById() devrait récupérer un élément', () => {
    const mockData = { id: 1 } as SalleResponseDTO;

    service.findById(1).subscribe(data => {
      expect(data).toEqual(mockData);
    });

    const req = httpMock.expectOne(r => r.url.includes(apiUrl + '/1') && r.method === 'GET');
    req.flush(mockData);
  });

  it('create() devrait envoyer une requête POST', () => {
    const dto = {} as SalleCreateDTO;
    const created = { id: 1 } as SalleResponseDTO;

    service.create(dto).subscribe(data => {
      expect(data).toEqual(created);
    });

        const req = httpMock.expectOne(r => r.url.includes(apiUrl) && r.method === 'POST');
    expect(req.request.body).toEqual(dto);
    req.flush(created);
  });

  it('update() devrait envoyer une requête PUT', () => {
    const dto = {} as SalleUpdateDTO;
    const updated = { id: 1 } as SalleResponseDTO;

    service.update(1, dto).subscribe(data => {
      expect(data).toEqual(updated);
    });

        const req = httpMock.expectOne(r => r.url.includes(apiUrl + '/1') && r.method === 'PUT');
    expect(req.request.body).toEqual(dto);
    req.flush(updated);
  });

  it('delete() devrait envoyer une requête DELETE', () => {
    service.delete(1).subscribe(response => {
      expect(response).toBeNull();
    });

        const req = httpMock.expectOne(r => r.url.includes(apiUrl + '/1') && r.method === 'DELETE');
    req.flush(null);
  });

});
