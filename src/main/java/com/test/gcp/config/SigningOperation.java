package com.test.gcp.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class SigningOperation {
	@NonNull
	private String operationMode;
	private String gcsBucket = null;
	private String gcsObjectPath = null;
	private int timeoutInMins = 15;
	private String rsaKeyID = null;

	public OperationMode toOperationMode() {
		return OperationMode.toOperationMode(operationMode);
	}
}
