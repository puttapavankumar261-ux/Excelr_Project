package com.emp.manag.schedule.dto;

import lombok.Data;

@Data
public class LeaveSummaryDTO {

    private Integer totalLeaves;

    private Integer pendingLeaves;

    private Integer approvedLeaves;

    private Integer rejectedLeaves;

    private Integer cancelledLeaves;
}