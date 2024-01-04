package rs.raf.demo.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleOperationRequest {
    private Long id;
    private LocalDateTime dateTime;
}
