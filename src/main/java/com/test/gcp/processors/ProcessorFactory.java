package com.test.gcp.processors;

import com.test.gcp.config.DesktopAppConfig;
import com.test.gcp.config.SigningOperation;
import com.test.gcp.config.SigningOperation.OperationMode;

public class ProcessorFactory {

	private ProcessorFactory() {
	}

	public static ISigningProcessor getSigningProcessor(SigningOperation appMode, DesktopAppConfig desktopAppConfig) {

		switch (appMode.toOperationMode()) {
		case LIST_KEYS:
			return new ListAccountsProcessor(desktopAppConfig);
		case LIST_HMAC_KEYS:
			return new EmptyProcessor(desktopAppConfig, OperationMode.LIST_HMAC_KEYS);
		case GEN_RSA_KEY:
			return new EmptyProcessor(desktopAppConfig, OperationMode.GEN_RSA_KEY);
		case GEN_HMAC_KEY:
			return new EmptyProcessor(desktopAppConfig, OperationMode.GEN_HMAC_KEY);
		case DEACTIVATE_RSA_KEY:
			return new EmptyProcessor(desktopAppConfig, OperationMode.DEACTIVATE_RSA_KEY);
		case DEACTIVATE_HMAC_KEY:
			return new EmptyProcessor(desktopAppConfig, OperationMode.DEACTIVATE_HMAC_KEY);
		}

		throw new RuntimeException("Failed to find operation for provided SigningOperation config string.");
	}

}
