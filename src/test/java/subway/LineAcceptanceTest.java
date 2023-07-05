package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.controller.dto.request.LineCreateRequest;
import subway.controller.dto.request.LineUpdateRequest;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.StationResponse;
import subway.util.DatabaseCleaner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LineFixture.*;
import static subway.fixture.StationFixture.지하철역_생성;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // when
        LineCreateRequest request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역").getId(),
                지하철역_생성("양재역").getId(),
                10L
        );
        지하철_노선_생성(request);

        // then
        List<LineResponse> allLineResponse = 지하철_노선_전체_조회();
        assertThat(allLineResponse).hasSize(1);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     * */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines() {
        // given
        LineCreateRequest request1 = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역").getId(),
                지하철역_생성("양재역").getId(),
                10L
        );
        LineCreateRequest request2 = new LineCreateRequest(
                "2호선",
                "bg-red-600",
                지하철역_생성("사당역").getId(),
                지하철역_생성("방배역").getId(),
                10L
        );
        지하철_노선_생성(request1);
        지하철_노선_생성(request2);

        // when
        List<LineResponse> response = 지하철_노선_전체_조회();

        // then
        assertThat(response).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     * */
    @DisplayName("지하철노선 조회")
    @Test
    void getLine() {
        // given
        LineCreateRequest request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역").getId(),
                지하철역_생성("양재역").getId(),
                10L
        );
        Long createLineId = 지하철_노선_생성(request).getId();

        // when
        LineResponse response = 지하철_노선_조회(createLineId);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getColor()).isEqualTo(request.getColor()),
                () -> assertThat(response.getStations().stream().map(StationResponse::getId).collect(Collectors.toList()))
                        .containsExactly(request.getUpStationId(), request.getDownStationId())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     * */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        // given
        LineCreateRequest request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역").getId(),
                지하철역_생성("양재역").getId(),
                10L
        );
        Long createLineId = 지하철_노선_생성(request).getId();

        // when
        LineUpdateRequest updateRequest = new LineUpdateRequest("다른분당선", "bg-black-600");
        지하철_노선_수정(createLineId, updateRequest);

        // then
        LineResponse updatedLine = 지하철_노선_조회(createLineId);
        assertAll(
                () -> assertThat(updatedLine.getName()).isEqualTo(updateRequest.getName()),
                () -> assertThat(updatedLine.getColor()).isEqualTo(updateRequest.getColor())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     * */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        // given
        LineCreateRequest request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역").getId(),
                지하철역_생성("양재역").getId(),
                10L
        );
        Long createLineId = 지하철_노선_생성(request).getId();

        // when
        지하철_노선_삭제(createLineId);

        // then
        List<Long> findLineIds = 지하철_노선_전체_조회().stream().map(LineResponse::getId).collect(Collectors.toList());
        assertThat(findLineIds).doesNotContain(createLineId);
    }
}

