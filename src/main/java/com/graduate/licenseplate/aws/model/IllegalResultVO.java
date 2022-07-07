package com.graduate.licenseplate.aws.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class IllegalResultVO {
	private String status;
	private String message;
	private String allPageCount;
	private List<IllegalImage> illegalList;  
} 
//{
//	"status" : "",
//	"message": "",
//	"illegalList" : {
//		{
//			"phone": "",
//			"userName": "",
//			"date": "",
//			"licenseplate": "",
//			"lpUrl": "",
//			"originUrl": ""
//		}
//		{
//			"phone": "",
//			"userName": "",
//			"date": "",
//			"licenseplate": "",
//			"lpUrl": "",
//			"originUrl": ""
//		}
//		{
//			"phone": "",
//			"userName": "",
//			"date": "",
//			"licenseplate": "",
//			"lpUrl": "",
//			"originUrl": ""
//		}
//	}
//}