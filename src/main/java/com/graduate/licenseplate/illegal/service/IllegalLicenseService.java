package com.graduate.licenseplate.illegal.service;

import com.graduate.licenseplate.aws.model.IllegalResultVO;
import com.graduate.licenseplate.illegal.model.IllegalCondition;

public interface IllegalLicenseService {

	IllegalResultVO getUrlWithCondition(IllegalCondition illegalCondition);

	IllegalResultVO getUrlWithHuman(IllegalCondition illegalCondition);

}
