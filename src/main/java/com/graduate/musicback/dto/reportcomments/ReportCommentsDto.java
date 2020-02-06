package com.graduate.musicback.dto.reportcomments;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCommentsDto {
    private String commentsId;
    private String reason;
}
