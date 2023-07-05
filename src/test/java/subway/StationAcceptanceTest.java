package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import subway.controller.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.StationFixture.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    public static final String KANGNAM_STATION = "강남역";
    public static final String YEOKSAM_STATION = "역삼역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Long createdId = 지하철역_생성(KANGNAM_STATION).getId();

        // then
        List<Long> stationIds = 지하철역_전체_조회().stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(stationIds).containsAnyOf(createdId);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 전체 조회한다.")
    @Test
    void getStations() {
        // given
        지하철역_생성(KANGNAM_STATION);
        지하철역_생성(YEOKSAM_STATION);

        // when
        List<StationResponse> responses = 지하철역_전체_조회();

        // then
        List<String> stationsNames = responses.stream().map(StationResponse::getName).collect(Collectors.toList());
        assertThat(stationsNames).containsExactly(KANGNAM_STATION, YEOKSAM_STATION);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        Long stationId = 지하철역_생성(KANGNAM_STATION).getId();

        // when
        지하철역_삭제(stationId);

        // then
        List<Long> findStationsIds = 지하철역_전체_조회().stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(findStationsIds).doesNotContain(stationId);
    }
}
