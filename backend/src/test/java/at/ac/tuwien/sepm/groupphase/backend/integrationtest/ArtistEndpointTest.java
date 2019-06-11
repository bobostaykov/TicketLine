package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTestWithMockedUserCredentials;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;

public class ArtistEndpointTest extends BaseIntegrationTestWithMockedUserCredentials {

    private static final String ARTIST_ENDPOINT = "/artists?artist_name=Artist1";
    private static final Long ARTIST_ID = 1L;
    private static final String ARTIST_NAME = "Artist1";

    @MockBean
    private ArtistRepository artistRepository;

    @Test
    public void findArtistsByNameUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(ARTIST_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }


    @Test
    public void findArtistsByNameAsUser() {
        BDDMockito
            .given(artistRepository.findByNameContainingIgnoreCase(ARTIST_NAME))
            .willReturn(Collections.singletonList(
                Artist.builder()
                    .id(ARTIST_ID)
                    .name(ARTIST_NAME)
                    .build()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(ARTIST_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(ArtistDTO[].class)), is(Collections.singletonList(
            ArtistDTO.builder()
                .id(ARTIST_ID)
                .name(ARTIST_NAME)
                .build())));
    }


    @Test
    public void findArtistsByNameAsAdmin() {
        BDDMockito
            .given(artistRepository.findByNameContainingIgnoreCase(ARTIST_NAME))
            .willReturn(Collections.singletonList(
                Artist.builder()
                    .id(ARTIST_ID)
                    .name(ARTIST_NAME)
                    .build()
            ));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(ARTIST_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(ArtistDTO[].class)), is(Collections.singletonList(
            ArtistDTO.builder()
                .id(ARTIST_ID)
                .name(ARTIST_NAME)
                .build())));
    }

}
