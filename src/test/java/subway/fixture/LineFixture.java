package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.request.LineCreateRequest;
import subway.controller.dto.request.LineUpdateRequest;
import subway.controller.dto.response.LineCreateResponse;
import subway.controller.dto.response.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineFixture {
    public static LineCreateResponse 지하철_노선_생성(LineCreateRequest request) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.jsonPath().getObject("", LineCreateResponse.class);
    }

    public static void 지하철_노선_수정(Long lineId, LineUpdateRequest request) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", lineId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제(Long lineId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/{id}", lineId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static List<LineResponse> 지하철_노선_전체_조회() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getList("", LineResponse.class);
    }

    public static LineResponse 지하철_노선_조회(Long lineId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/{id}", lineId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getObject("", LineResponse.class);
    }
}
