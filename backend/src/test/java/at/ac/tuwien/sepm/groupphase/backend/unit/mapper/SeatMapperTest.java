package at.ac.tuwien.sepm.groupphase.backend.unit.mapper;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat.SeatDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.seat.SeatMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class SeatMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.sepm.groupphase.backend.entity.mapper")
    public static class SeatMapperTestContextConfiguration{

    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private SeatMapper seatMapper;

    private static final Long SEAT_ID = 1L;
    private static final Integer SEAT_NUMBER = 10;
    private static final Integer SEAT_ROW = 2;
    private static final PriceCategory PRICE_CATEGORY = PriceCategory.AVERAGE;

    @Test
    public void shouldMapSeatDTOToSeat(){
        SeatDTO seatDTO = SeatDTO.builder()
            .id(SEAT_ID)
            .seatNumber(SEAT_NUMBER)
            .seatRow(SEAT_ROW)
            .priceCategory(PRICE_CATEGORY)
            .build();
        Seat seat = seatMapper.seatDTOToSeat(seatDTO);
        assertThat(seat).isNotNull();
        assertThat(seat.getId()).isEqualTo(SEAT_ID);
        assertThat(seat.getSeatNumber()).isEqualTo(SEAT_NUMBER);
        assertThat(seat.getSeatRow()).isEqualTo(SEAT_ROW);
        assertThat(seat.getPriceCategory()).isEqualTo(PRICE_CATEGORY);
    }

    @Test
    public void shouldMapSeatToSeatDTO(){
        Seat seat = Seat.builder()
            .id(SEAT_ID)
            .seatNumber(SEAT_NUMBER)
            .seatRow(SEAT_ROW)
            .priceCategory(PRICE_CATEGORY)
            .build();
        SeatDTO seatDTO = seatMapper.seatToSeatDTO(seat);
        assertThat(seatDTO).isNotNull();
        assertThat(seatDTO.getId()).isEqualTo(SEAT_ID);
        assertThat(seatDTO.getSeatNumber()).isEqualTo(SEAT_NUMBER);
        assertThat(seatDTO.getSeatRow()).isEqualTo(SEAT_ROW);
        assertThat(seatDTO.getPriceCategory()).isEqualTo(PRICE_CATEGORY);
    }
}
