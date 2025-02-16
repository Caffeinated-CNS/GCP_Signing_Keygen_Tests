package com.test.gcp.processors;

import com.test.gcp.config.DesktopAppConfig;
import com.test.gcp.config.SigningOperation;

public class ProcessorFactory {

	private ProcessorFactory() {
	}

	public static ISigningProcessor getSigningProcessor(SigningOperation appMode, DesktopAppConfig desktopAppConfig) {

		switch (appMode.toOperationMode()) {
		case LIST_KEYS:
			return new ListAccountsProcessor(desktopAppConfig);
		}

		throw new RuntimeException("Failed to find operation for provided SigningOperation config string.");
	}

}
