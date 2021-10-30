import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MaprRequestsService } from '../service/mapr-requests.service';

import { MaprRequestsComponent } from './mapr-requests.component';

describe('MaprRequests Management Component', () => {
  let comp: MaprRequestsComponent;
  let fixture: ComponentFixture<MaprRequestsComponent>;
  let service: MaprRequestsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MaprRequestsComponent],
    })
      .overrideTemplate(MaprRequestsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaprRequestsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MaprRequestsService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.maprRequests?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
