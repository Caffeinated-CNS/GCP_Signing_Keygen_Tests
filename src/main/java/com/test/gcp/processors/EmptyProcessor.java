package com.test.gcp.processors;

import com.test.gcp.config.DesktopAppConfig;
import com.test.gcp.config.SigningOperation.OperationMode;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class EmptyProcessor implements ISigningProcessor {
	@NonNull
	DesktopAppConfig desktopAppConfig;
	@NonNull
	OperationMode operationMode;

	@Override
	public void process() {
		// Do nothing meaningful.
		System.out.println("EmptyProcessor called.");
	}

	@Override
	public String getSelfDescription() {
		return String.format("EmptyProcessor / no processor implementation for %s operation.", operationMode.toString());
	}

}
