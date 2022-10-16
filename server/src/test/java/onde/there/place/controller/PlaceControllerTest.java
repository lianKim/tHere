package onde.there.place.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import onde.there.domain.Journey;
import onde.there.domain.Place;
import onde.there.domain.type.PlaceCategoryType;
import onde.there.dto.place.PlaceDto;
import onde.there.exception.PlaceException;
import onde.there.exception.type.ErrorCode;
import onde.there.place.service.PlaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PlaceController.class)
class PlaceControllerTest {

	@MockBean
	private PlaceService placeService;

	@MockBean
	private PlaceDto.Response response;

	@InjectMocks
	private PlaceController placeController;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mvc;


	@DisplayName("01_00. /place/get?placeId=1  success")
	@Test
	public void test_01_00() throws Exception {
		//given
		given(placeService.getPlace(any())).willReturn(testPlace(1L));

		//when
		mvc.perform(get("/place/get?placeId=1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.placeId").value(1L))
			.andExpect(jsonPath("$.title").value("장소 테스트 제목"))
			.andExpect(jsonPath("$.text").value("장소 테스트 본문"))
			.andExpect(jsonPath("$.addressName").value("장소 테스르 전체 주소"))
			.andExpect(jsonPath("$.placeHeartSum").value(0))
			.andExpect(jsonPath("$.placeCategory").value("기타"))
			.andDo(print());
		//then
	}

	@DisplayName("01_01. /place/get?placeId=1  fail not found place")
	@Test
	public void test_01_01() throws Exception {
		//given
		given(placeService.getPlace(any())).willThrow(
			new PlaceException(ErrorCode.NOT_FOUND_PLACE));

		System.out.println(ErrorCode.NOT_FOUND_PLACE.getDescription());
		//when
		mvc.perform(get("/place/get?placeId=1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND_PLACE.toString()))
			.andExpect(jsonPath("$.errorMessage").value(ErrorCode.NOT_FOUND_PLACE.getDescription()))
			.andDo(print());
		//then
	}




	private static Place testPlace(Long id) {
		return Place.builder()
			.id(id)
			.title("장소 테스트 제목")
			.text("장소 테스트 본문")
			.addressName("장소 테스르 전체 주소")
			.placeHeartSum(0L)
			.journey(Journey.builder().build())
			.placeCategory(PlaceCategoryType.ECT)
			.build();
	}
}