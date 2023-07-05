package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.response.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationFixture {
    public static StationResponse 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.jsonPath().getObject("", StationResponse.class);
    }

    public static void 지하철역_삭제(Long stationId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/stations/{id}", stationId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static List<StationResponse> 지하철역_전체_조회() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getList("", StationResponse.class);
    }
}
