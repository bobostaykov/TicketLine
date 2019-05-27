package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

/*
public class HallEndpointTest extends BaseIntegrationTest {

    private static final String HALL_ENDPOINT = "/halls";
    private static final Long HALL_ID = 1L;
    private static final String HALL_NAME = "Test_Hall";
    private static Location hall_location;
    private static LocationDTO hall_location_dto;
    private static List<Seat> seat_list;
    private static List<SeatDTO> seat_dto_list;

    // TODO: Unfinished Tests

    @MockBean
    HallRepository hallRepository;

    //initialize corresponding variables
    @BeforeClass
    public static void init(){
        Long id = 1L;
        String country = "Austria", city = "Vienna", postalCode = "1010",
            street = "Karlsplatz", description = "None";
        hall_location = Location.builder()
            .id(id)
            .country(country)
            .city(city)
            .postalCode(postalCode)
            .street(street)
            .description(description)
            .build();
        hall_location_dto = LocationDTO.builder()
            .id(id)
            .country(country)
            .city(city)
            .postalCode(postalCode)
            .street(street)
            .description(description)
            .build();
        seat_list = Collections.singletonList(
            Seat.builder()
            .id(1L)
            .seatNumber(1)
            .seatRow(1)
            .priceCategory(PriceCategory.AVERAGE)
            .build()
        );
        seat_dto_list = Collections.singletonList(
            SeatDTO.builder()
            .id(1L)
            .seatNumber(1)
            .seatRow(1)
            .priceCategory(PriceCategory.AVERAGE)
            .build()
        );
    }

    @Test
    public void findAllHalls_unauthorized() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response, is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllHalls_authorizedAsUser() {
        BDDMockito
            .given(hallRepository.findAll())
            .willReturn(
                Collections.singletonList(
                    Hall.builder()
                        .id(HALL_ID)
                        .name(HALL_NAME)
                        .location(hall_location)
                        .seats(seat_list)
                        .build()
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Assert.assertThat(Arrays.asList(response.as(HallDTO.class)), is(Collections.singletonList(
            HallDTO.builder()
            .id(HALL_ID)
            .name(HALL_NAME)
            .location(hall_location_dto)
            .seats(seat_dto_list)
            .build()
        )));
    }
}
*/

