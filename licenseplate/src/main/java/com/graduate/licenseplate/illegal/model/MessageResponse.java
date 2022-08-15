package com.graduate.licenseplate.illegal.model;

import java.util.List;

import com.graduate.licenseplate.aws.model.IllegalImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MessageResponse {
	private String status;
	private String message;
}
